import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;

public class GameWindow {
    private JFrame frame;
    private JPanel panel;
    private AsteroidProgressBar asteroidProgressBar;
    private JLabel debug;
    private boolean debugMode = false;
    private CreditsWindow credits;
    private JButton soundToggleButton;
    private List<ImageIcon> backgroundFrames;
    private int currentFrameIndex = 0;
    private Timer backgroundTimer;
    private JTextField asteroidCountField;
    private JButton playButton;
    private boolean gameRunning = false;
    private boolean creatingAsteroids = false;

    public GameWindow() {
        frame = new JFrame("Asteroid Game ");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(Config.WINDOW_WIDTH, Config.WINDOW_HEIGHT);
        frame.setUndecorated(true);

        try {
            Image cursorImage = Toolkit.getDefaultToolkit().createImage(System.getProperty("user.dir") + File.separator
                    + "assets" + File.separator + "images" + File.separator + "hand_thin_small_point.png");
            Cursor customCursor = Toolkit.getDefaultToolkit().createCustomCursor(cursorImage, new Point(0, 0), "hand");
            frame.setCursor(customCursor);
        } catch (Exception e) {
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
        backgroundFrames = new ArrayList<>();
        for (int i = 0; i < 60; i++) {
            String framePath = System.getProperty("user.dir") + File.separator + "assets" + File.separator
                    + "background" + File.separator + "frame" + File.separator + String.format("frame_%03d.png", i);
            try {
                Image frameImage = Toolkit.getDefaultToolkit().createImage(framePath);
                ImageIcon frameIcon = new ImageIcon(frameImage);
                backgroundFrames.add(frameIcon);
            } catch (Exception e) {
                System.out.println("Cannot load frame: " + framePath);
            }
        }

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());

        panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);

                try {
                    Image hudBackground = Toolkit.getDefaultToolkit().createImage(System.getProperty("user.dir")
                            + File.separator + "assets" + File.separator + "ui" + File.separator + "TitlePanel01.png");
                    MediaTracker tracker = new MediaTracker(this);
                    tracker.addImage(hudBackground, 0);
                    tracker.waitForAll();

                    int hudHeight = 60;
                    int hudY = Config.WINDOW_HEIGHT - hudHeight;
                    int hudX = 0;
                    g.drawImage(hudBackground, hudX, hudY,
                            Config.WINDOW_WIDTH, hudHeight, this);
                } catch (Exception e) {
                    g.setColor(new Color(0, 0, 0, 150));
                    g.fillRect(0, Config.WINDOW_HEIGHT - 60, Config.WINDOW_WIDTH, 60);
                }
            }
        };
        panel.setLayout(null);
        panel.setOpaque(false);
        panel.setFocusable(true);
        panel.addKeyListener(new KeyAdapter() {
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
        JLabel backgroundLabel = new JLabel();
        if (!backgroundFrames.isEmpty()) {
            backgroundLabel.setIcon(backgroundFrames.get(0));
        }
        backgroundLabel.setLayout(new BorderLayout());
        backgroundLabel.add(panel, BorderLayout.CENTER);
        mainPanel.add(backgroundLabel, BorderLayout.CENTER);

        backgroundTimer = new Timer(80, _ -> {
            if (!backgroundFrames.isEmpty()) {
                currentFrameIndex = (currentFrameIndex + 1) % backgroundFrames.size();
                backgroundLabel.setIcon(backgroundFrames.get(currentFrameIndex));
            }
        });
        backgroundTimer.start();

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

        soundToggleButton = new JButton();
        soundToggleButton.setBounds(Config.WINDOW_WIDTH - 70, 15, 32, 32);
        soundToggleButton.setOpaque(false);
        soundToggleButton.setContentAreaFilled(false);
        soundToggleButton.setBorderPainted(false);
        soundToggleButton.setFocusPainted(false);
        updateSoundButtonIcon();
        soundToggleButton.addActionListener(_ -> {
            SoundManager.toggleSound();
            updateSoundButtonIcon();
            frame.requestFocus();
        });
        panel.add(soundToggleButton);

        setupControlPanel();

        frame.add(mainPanel);
        frame.setVisible(true);
        frame.requestFocus();
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
            debug.setText("Debug Mode: ON (Press T to ON-OFF)");
            debug.setLocation(10, 50);
            debug.setVisible(true);
        } else {
            debug.setText("");
            debug.setVisible(false);
        }
    }

    private void updateSoundButtonIcon() {
        try {
            String iconPath = System.getProperty("user.dir") + File.separator + "assets" + File.separator + "ui"
                    + File.separator +
                    (SoundManager.isSoundEnabled() ? "Music_On.png" : "Music_Off.png");
            Image iconImage = Toolkit.getDefaultToolkit().createImage(iconPath);
            ImageIcon icon = new ImageIcon(iconImage);
            soundToggleButton.setIcon(icon);
        } catch (Exception e) {
        }
    }

    private void setupControlPanel() {

        int hudY = Config.WINDOW_HEIGHT - 60;
        int hudX = 150;

        JLabel emoteLabel = new JLabel();
        try {
            Image emoteImage = Toolkit.getDefaultToolkit().createImage(System.getProperty("user.dir") + File.separator
                    + "assets" + File.separator + "images" + File.separator + "emote.gif");
            ImageIcon emoteIcon = new ImageIcon(emoteImage);
            emoteLabel.setIcon(emoteIcon);
        } catch (Exception e) {
        }
        emoteLabel.setBounds(hudX + 20, hudY + 10, 30, 30);
        panel.add(emoteLabel);

        asteroidCountField = new JTextField("12", 3);
        asteroidCountField.setHorizontalAlignment(JTextField.CENTER);
        asteroidCountField.setFont(new Font("Tahoma", Font.BOLD, 12));
        asteroidCountField.setForeground(Color.WHITE);
        asteroidCountField.setOpaque(false);
        asteroidCountField.setBorder(BorderFactory.createLineBorder(Color.WHITE));
        asteroidCountField.setBounds(hudX + 90, hudY + 15, 90, 20);
        panel.add(asteroidCountField);

        playButton = new JButton();
        playButton.setOpaque(false);
        playButton.setContentAreaFilled(false);
        playButton.setBorderPainted(false);
        playButton.setFocusPainted(false);
        try {
            Image buttonImage = Toolkit.getDefaultToolkit().createImage(System.getProperty("user.dir") + File.separator
                    + "assets" + File.separator + "ui" + File.separator + "Button08.png");
            ImageIcon buttonIcon = new ImageIcon(buttonImage);
            playButton.setIcon(buttonIcon);
        } catch (Exception e) {
        }
        playButton.setBounds(hudX + 190, hudY + 15, 70, 30);
        playButton.addActionListener(_ -> {
            if (!gameRunning && !creatingAsteroids) {
                startGame();
            }
            frame.requestFocus();
        });
        panel.add(playButton);
    }

    private void startGame() {
        try {
            int count = Integer.parseInt(asteroidCountField.getText());
            if (count < 1)
                count = 1;

            debugMode = false;
            debug.setVisible(debugMode);
            updateDebugInfo();
            debug.setText("");

            creatingAsteroids = true;
            gameRunning = true;

            App.clearAndRestart(count, panel, asteroidProgressBar, this, () -> {
                creatingAsteroids = false;
            });

            new Thread(() -> {
                try {
                    Thread.sleep(2000);
                    gameRunning = false;
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }).start();
        } catch (NumberFormatException e) {
            asteroidCountField.setText("10");
        }
    }

}
