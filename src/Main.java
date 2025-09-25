import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        String input = JOptionPane.showInputDialog(null,
                "Enter number of asteroids (" + Config.MIN_ASTEROIDS + "):",
                String.valueOf(Config.DEFAULT_ASTEROIDS));
        int asteroidCount = Config.DEFAULT_ASTEROIDS;
        try {
            asteroidCount = Integer.parseInt(input);
            if (asteroidCount < Config.MIN_ASTEROIDS)

                asteroidCount = Config.MIN_ASTEROIDS;
        } catch (NumberFormatException e) {
            asteroidCount = Config.DEFAULT_ASTEROIDS;
        }

        GameWindow gameWindow = new GameWindow();
        App.setAsteroidCount(asteroidCount);
        App.start(gameWindow.getAsteroidPanel(), gameWindow.getAsteroidProgressBar(), gameWindow);
        SoundManager.playBackground();
    }
}