import java.util.List;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;
import javax.swing.*;

public class App {
    private static final List<Asteroid> asteroids = new CopyOnWriteArrayList<>();
    private static JPanel panel;
    private static JLabel info;
    private static int count = Config.DEFAULT_ASTEROIDS;
    private static Thread gameThread;
    private static GameWindow window;
    private static boolean autoSpawn = false;
    private static long lastSpawnTime = 0;

    public static List<Asteroid> getAsteroids() {
        return asteroids;
    }

    public static void setAsteroidCount(int count) {
        App.count = count;
    }

    public static void toggleAutoSpawn() {
        autoSpawn = !autoSpawn;
    }

    public static boolean isAutoSpawnEnabled() {
        return autoSpawn;
    }

    static void start(JPanel panel, JLabel label, GameWindow window) {
        App.panel = panel;
        App.info = label;
        App.window = window;

        Random r = new Random();

        for (int i = 0; i < count; i++) {
            int x = r.nextInt(Config.WINDOW_WIDTH - Config.ASTEROID_SIZE - 200) + 100;
            int y = r.nextInt(Config.WINDOW_HEIGHT - Config.ASTEROID_SIZE - 200) + 100;

            int dx = r.nextInt(8) - 4;
            int dy = r.nextInt(8) - 4;

            if (dx == 0)
                dx = 1;
            if (dy == 0)
                dy = 1;

            Asteroid a = new Asteroid(i, x, y, dx, dy, panel);
            asteroids.add(a);
            panel.add(a.getLabel());

            Thread t = new Thread(a, "Asteroid-" + i);
            t.setDaemon(true);
            t.start();
        }

        startGameLoop();
    }

    private static void spawnAsteroid() {
        Random r = new Random();
        int x = r.nextInt(Config.WINDOW_WIDTH - Config.ASTEROID_SIZE - 200) + 100;
        int y = r.nextInt(Config.WINDOW_HEIGHT - Config.ASTEROID_SIZE - 200) + 100;
        
        int dx = r.nextInt(8) - 4;
        int dy = r.nextInt(8) - 4;
        
        if (dx == 0) dx = 1;
        if (dy == 0) dy = 1;
        
        Asteroid a = new Asteroid(asteroids.size(), x, y, dx, dy, panel);
        asteroids.add(a);
        panel.add(a.getLabel());
        
        Thread t = new Thread(a, "Asteroid-" + asteroids.size());
        t.setDaemon(true);
        t.start();
    }

    private static void startGameLoop() {
        gameThread = new Thread(() -> {
            while (true) {
                try {
                    SwingUtilities.invokeLater(() -> {
                        info.setText("Asteroids: " + asteroids.size());
                        asteroids.removeIf(asteroid -> !asteroid.isAlive());
                        if (window != null && window.isDebugMode()) {
                            window.updateDebugInfo();
                        }
                        
                        if (autoSpawn && asteroids.size() < count && System.currentTimeMillis() - lastSpawnTime > 2000) {
                            spawnAsteroid();
                            lastSpawnTime = System.currentTimeMillis();
                        }
                    });
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    break;
                }
            }
        }, "GameLoop");
        gameThread.setDaemon(true);
        gameThread.start();
    }
}
