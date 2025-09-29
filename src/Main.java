
public class Main {
    public static void main(String[] args) {
        int asteroidCount = 0;
        GameWindow gameWindow = new GameWindow();
        App.setAsteroidCount(asteroidCount);
        App.start(gameWindow.getAsteroidPanel(), gameWindow.getAsteroidProgressBar(), gameWindow);
        SoundManager.playBackground();
    }
}