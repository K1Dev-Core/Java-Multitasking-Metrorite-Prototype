import java.util.List;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;
import javax.swing.*;

public class App {
    private static final List<Asteroid> asteroids = new CopyOnWriteArrayList<>();
    private static JPanel panel;
    private static AsteroidProgressBar asteroidProgressBar;
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

    public static void start(JPanel panel, AsteroidProgressBar asteroidProgressBar, GameWindow window) {
        App.panel = panel;
        App.asteroidProgressBar = asteroidProgressBar;
        App.window = window;

        asteroidProgressBar.setMaxAsteroids(count);

        Random r = new Random();

        for (int i = 0; i < count; i++) {
            int marginX = 111;
            int marginY = 120;
            int x = r.nextInt(Config.WINDOW_WIDTH - Config.ASTEROID_SIZE - marginX * 2) + marginX;
            int y = r.nextInt(Config.WINDOW_HEIGHT - Config.ASTEROID_SIZE - marginY * 2) + marginY;

            int dx = r.nextInt(7) - 3;
            int dy = r.nextInt(7) - 3;

            if (dx == 0)
                dx = 1;
            if (dy == 0)
                dy = 1;

            Asteroid a = new Asteroid(i, x, y, dx, dy, panel);
            asteroids.add(a);
            if (panel != null) {
                panel.add(a.getLabel());
            }

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

        int dx = r.nextInt(10) - 5;
        int dy = r.nextInt(10) - 5;

        if (dx == 0)
            dx = 1;
        if (dy == 0)
            dy = 1;

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
                    asteroidProgressBar.setAsteroidCount(asteroids.size());
                    asteroids.removeIf(asteroid -> !asteroid.isAlive());
                    if (window != null && window.isDebugMode() && System.currentTimeMillis() % 200 < 50) {
                        window.updateDebugInfo();
                    }

                    if (autoSpawn && asteroids.size() < count && System.currentTimeMillis() - lastSpawnTime > 2000) {
                        spawnAsteroid();
                        lastSpawnTime = System.currentTimeMillis();
                    }
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    break;
                }
            }
        }, "GameLoop");
        gameThread.setDaemon(true);
        gameThread.start();
    }

    public static void clearAndRestart(int newCount, JPanel panel, AsteroidProgressBar asteroidProgressBar,
            GameWindow window, Runnable onComplete) {
        App.panel = panel;
        App.asteroidProgressBar = asteroidProgressBar;
        App.window = window;

        Thread cleanupThread = new Thread(() -> {
            for (Asteroid asteroid : asteroids) {
                asteroid.stop();
            }

            if (panel != null) {
                for (Asteroid asteroid : asteroids) {
                    if (asteroid.getLabel() != null) {
                        panel.remove(asteroid.getLabel());
                    }
                }
                panel.revalidate();
                panel.repaint();
            }

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }

            asteroids.clear();
            count = newCount;
            asteroidProgressBar.setMaxAsteroids(count);

            Random r = new Random();

            for (int i = 0; i < count; i++) {
                int x = r.nextInt(Config.WINDOW_WIDTH - Config.ASTEROID_SIZE - 200) + 100;
                int y = r.nextInt(Config.WINDOW_HEIGHT - Config.ASTEROID_SIZE - 200) + 100;

                int dx = r.nextInt(7) - 3;
                int dy = r.nextInt(7) - 3;

                if (dx == 0)
                    dx = 1;
                if (dy == 0)
                    dy = 1;

                Asteroid a = new Asteroid(i, x, y, dx, dy, panel);
                asteroids.add(a);
                if (panel != null) {
                    panel.add(a.getLabel());
                }

                Thread t = new Thread(a, "Asteroid-" + i);
                t.setDaemon(true);
                t.start();
            }

            panel.revalidate();
            panel.repaint();

            if (onComplete != null) {
                onComplete.run();
            }
        });
        cleanupThread.setDaemon(true);
        cleanupThread.start();

        if (gameThread == null) {
            startGameLoop();
        }
    }

    public static void clearAndRestart(int newCount, JPanel panel, AsteroidProgressBar asteroidProgressBar,
            GameWindow window) {
        clearAndRestart(newCount, panel, asteroidProgressBar, window, null);
    }
}
