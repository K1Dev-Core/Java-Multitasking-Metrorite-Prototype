import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.*;

public class GameWindow {
    private JFrame frame;
    private JPanel panel;
    private AsteroidProgressBar asteroidProgressBar;
    private JLabel debug;
    private boolean debugMode = false;
    private CreditsWindow credits;

    public GameWindow() {
        frame = new JFrame("Asteroid Game ");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(Config.WINDOW_WIDTH, Config.WINDOW_HEIGHT);
        
        try {
            Image cursorImage = ImageIO.read(new File("assets/images/hand_thin_small_point.png"));
            Cursor customCursor = Toolkit.getDefaultToolkit().createCustomCursor(cursorImage, new Point(0, 0), "hand");
            frame.setCursor(customCursor);
        } catch (IOException e) {
        }
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
                } else if (e.getKeyCode() == KeyEvent.VK_A && debugMode) {
                    App.toggleAutoSpawn();
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
        ImageIcon backgroundIcon = null;
        Image backgroundImage = null;
        try {
            backgroundIcon = new ImageIcon("assets/background/space_scroll_blend_fixed.gif");
            backgroundImage = backgroundIcon.getImage();
        } catch (Exception e) {
            System.out.println("Cannot load background GIF: " + e.getMessage());
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

        asteroidProgressBar = new AsteroidProgressBar();
        asteroidProgressBar.setBounds(10, 10, 300, 50);
        panel.add(asteroidProgressBar);

        debug = new JLabel("", JLabel.LEFT);
        debug.setForeground(Color.YELLOW);
        debug.setFont(new Font("Tahoma", Font.PLAIN, 12));
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

    public AsteroidProgressBar getAsteroidProgressBar() {
        return asteroidProgressBar;
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
