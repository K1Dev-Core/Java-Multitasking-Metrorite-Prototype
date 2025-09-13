import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import javax.swing.*;

public class App {
    private static final List<Asteroid> asteroids = new ArrayList<>();
    private static JPanel asteroidPanel;
    private static JLabel infoLabel;
    private static Timer gameTimer;
    private static int asteroidCount = Config.DEFAULT_ASTEROIDS;
    
    public static List<Asteroid> getAsteroids() {
        return asteroids;
    }
    
    public static void setAsteroidCount(int count) {
        asteroidCount = count;
    }

    static void start(JPanel panel, JLabel label) {
        asteroidPanel = panel;
        infoLabel = label;
        
        Random r = new Random();
        for (int i = 0; i < asteroidCount; i++) {
            int x = r.nextInt(Config.WINDOW_WIDTH - Config.ASTEROID_SIZE) + 25;
            int y = r.nextInt(Config.WINDOW_HEIGHT - Config.ASTEROID_SIZE) + 25;
            int dx = r.nextInt(8) - 4;
            int dy = r.nextInt(8) - 4;
            if (dx == 0) dx = 1;
            if (dy == 0) dy = 1;
            
            Asteroid asteroid = new Asteroid(i, x, y, dx, dy, asteroidPanel);
            asteroids.add(asteroid);
            asteroidPanel.add(asteroid.getLabel());
            
            Thread asteroidThread = new Thread(asteroid, "Asteroid-" + i);
            asteroidThread.start();
        }
        
        startGameLoop();
    }
    
    private static void startGameLoop() {
        gameTimer = new Timer(100, _ -> {
            infoLabel.setText("Asteroids: " + asteroids.size());
            
            asteroids.removeIf(asteroid -> !asteroid.isAlive());
        });
        gameTimer.start();
    }
}
