import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.imageio.ImageIO;
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
    private DraggablePlayer draggablePlayer;

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

        setupUI();
    }

    private void setupUI() {
        backgroundFrames = new ArrayList<>();
        for (int i = 0; i < 60; i++) {
            String framePath = String.format("assets/background/frame/frame_%03d.png", i);
            try {
                ImageIcon frameIcon = new ImageIcon(framePath);
                backgroundFrames.add(frameIcon);
            } catch (Exception e) {
                System.out.println("Cannot load frame: " + framePath);
            }
        }

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());

        panel = new JPanel();
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

        backgroundTimer = new Timer(80, e -> {
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
        soundToggleButton.addActionListener(e -> {
            SoundManager.toggleSound();
            updateSoundButtonIcon();
            panel.requestFocus();
        });
        panel.add(soundToggleButton);

        draggablePlayer = new DraggablePlayer();
        panel.add(draggablePlayer);
        panel.add(draggablePlayer.getEmoteLabel());

        frame.add(mainPanel);
        frame.setVisible(true);
        panel.requestFocus();
    }

    public JPanel getAsteroidPanel() {
        return panel;
    }

    public AsteroidProgressBar getAsteroidProgressBar() {
        return asteroidProgressBar;
    }

    public DraggablePlayer getDraggablePlayer() {
        return draggablePlayer;
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

    private void updateSoundButtonIcon() {
        try {
            String iconPath = SoundManager.isSoundEnabled() ? 
                "assets/ui/Music_On.png" : "assets/ui/Music_Off.png";
            ImageIcon icon = new ImageIcon(iconPath);
            soundToggleButton.setIcon(icon);
        } catch (Exception e) {
        }
    }
}
