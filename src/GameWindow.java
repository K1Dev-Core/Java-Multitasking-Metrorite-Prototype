import java.awt.*;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class GameWindow {
    private JFrame frame;
    private JPanel panel;
    private JLabel info;
    private JLabel debug;
    private boolean debugMode = false;
    private CreditsWindow credits;

    public GameWindow() {
        frame = new JFrame("Asteroid Game - Simple");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(Config.WINDOW_WIDTH, Config.WINDOW_HEIGHT);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);

        frame.setFocusable(true);
        frame.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_T) {
                    debugMode = !debugMode;
                    debug.setVisible(debugMode);
                    updateDebugInfo();
                } else if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                    if (credits == null) {
                        credits = new CreditsWindow();
                    }
                    if (credits.isVisible()) {
                        credits.hide();
                    } else {
                        credits.show();
                    }
                }
            }
        });

        setupUI();
    }

    private void setupUI() {
        Image backgroundImage = null;
        try {
            backgroundImage = ImageIO.read(new File("assets/background/Background_space.png"));
        } catch (IOException e) {
            System.out.println("Cannot load background image: " + e.getMessage());
        }

        final Image finalBackgroundImage = backgroundImage;

        JPanel mainPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (finalBackgroundImage != null) {
                    g.drawImage(finalBackgroundImage, 0, 0, getWidth(), getHeight(), this);
                } else {
                    g.setColor(Color.BLACK);
                    g.fillRect(0, 0, getWidth(), getHeight());
                }
            }
        };
        mainPanel.setLayout(new BorderLayout());

        panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (finalBackgroundImage != null) {
                    g.drawImage(finalBackgroundImage, 0, 0, getWidth(), getHeight(), this);
                } else {
                    g.setColor(Color.BLACK);
                    g.fillRect(0, 0, getWidth(), getHeight());
                }
            }
        };
        panel.setLayout(null);
        panel.setOpaque(false);
        mainPanel.add(panel, BorderLayout.CENTER);

        info = new JLabel("Asteroids: 0", JLabel.LEFT);
        info.setForeground(Color.WHITE);
        info.setFont(new Font("Tahoma", Font.BOLD, 16));
        info.setOpaque(false);
        info.setBounds(10, 10, 200, 30);
        panel.add(info);

        debug = new JLabel("", JLabel.LEFT);
        debug.setForeground(Color.YELLOW);
        debug.setFont(new Font("Monospaced", Font.PLAIN, 12));
        debug.setOpaque(false);
        debug.setBounds(10, 50, 400, 200);
        debug.setVisible(false);
        panel.add(debug);

        frame.add(mainPanel);
        frame.setVisible(true);
    }

    public JPanel getAsteroidPanel() {
        return panel;
    }

    public JLabel getInfoLabel() {
        return info;
    }

    public JLabel getDebugLabel() {
        return debug;
    }

    public boolean isDebugMode() {
        return debugMode;
    }

    public void updateDebugInfo() {
        for (Asteroid asteroid : App.getAsteroids()) {
            if (asteroid.isAlive()) {
                asteroid.updateDebugInfo(debugMode);
            }
        }

        if (debugMode) {
            debug.setText("Debug Mode: ON (Press T to toggle)");
            debug.setLocation(10, 50);
            debug.setVisible(true);
        } else {
            debug.setText("");
            debug.setVisible(false);
        }
    }
}
