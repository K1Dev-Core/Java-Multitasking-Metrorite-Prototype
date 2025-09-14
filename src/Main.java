import java.awt.*;
import javax.swing.*;

public class Main {
    public static void main(String[] args) {

        JFrame f = new JFrame("Asteroid Game - Simple");
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setSize(Config.WINDOW_WIDTH, Config.WINDOW_HEIGHT);
        f.setResizable(false);
        f.setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.setBackground(Color.BLACK);

        String input = JOptionPane.showInputDialog(f,
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

        JLabel infoLabel = new JLabel("Asteroids: " + asteroidCount, JLabel.CENTER);
        infoLabel.setForeground(Color.WHITE);
        infoLabel.setFont(new Font("Tahoma", Font.BOLD, 16));
        mainPanel.add(infoLabel, BorderLayout.NORTH);

        JPanel asteroidPanel = new JPanel();
        asteroidPanel.setLayout(null);
        asteroidPanel.setBackground(Color.BLACK);
        mainPanel.add(asteroidPanel, BorderLayout.CENTER);

        f.add(mainPanel);
        f.setVisible(true);

        App.setAsteroidCount(asteroidCount);
        App.start(asteroidPanel, infoLabel);
    }
}