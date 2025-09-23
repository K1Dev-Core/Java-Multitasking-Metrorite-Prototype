import java.awt.*;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class Asteroid implements Runnable {

    private int id;
    private int x, y;
    private int dx, dy;
    private boolean alive = true;
    private JLabel label;
    private JPanel parent;
    private ImageIcon icon;
    private ImageIcon bomb;
    private JLabel debug;

    public Asteroid(int id, int x, int y, int dx, int dy, JPanel parent) {
        this.id = id;
        this.x = x;
        this.y = y;
        this.dx = dx;
        this.dy = dy;
        this.parent = parent;

        try {
            int img = (id % 10) + 1;
            String path = "assets/images/" + img + ".png";
            String bombPath = "assets/images/bomb.gif";

            Image img1 = ImageIO.read(new File(path));
            img1 = img1.getScaledInstance(60, 60, Image.SCALE_SMOOTH);
            icon = new ImageIcon(img1);

            Image img2 = ImageIO.read(new File(bombPath));
            bomb = new ImageIcon(img2);

        } catch (IOException e) {
            System.out.println("Cannot load image: " + e.getMessage());
            icon = new ImageIcon();
            bomb = new ImageIcon();
        }

        this.label = new JLabel(icon);
        this.label.setBounds(x, y, Config.ASTEROID_SIZE, Config.ASTEROID_SIZE);

        this.label.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                explode();
            }
        });

        debug = new JLabel();
        debug.setForeground(Color.YELLOW);
        debug.setFont(new Font("Monospaced", Font.PLAIN, 7));
        debug.setOpaque(false);
        debug.setVisible(false);
        this.parent.add(debug);
    }

    public void explode() {
        if (bomb != null) {
            label.setIcon(bomb);
            int offset = (Config.BOMB_SIZE - Config.ASTEROID_SIZE) / 2;
            label.setBounds(x - offset, y - offset, Config.BOMB_SIZE, Config.BOMB_SIZE);
        }
        alive = false;

        SoundManager.playExplosion();

        Thread t = new Thread(() -> {
            try {
                Thread.sleep(Config.BOMB_DURATION);
                SwingUtilities.invokeLater(() -> {
                    parent.remove(label);
                    if (debug != null) {
                        parent.remove(debug);
                    }
                    parent.repaint();
                });
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }, "Explosion-" + id);
        t.setDaemon(true);
        t.start();
    }

    public JLabel getLabel() {
        return label;
    }

    public boolean isAlive() {
        return alive;
    }

    public int getId() {
        return id;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public JLabel getDebugLabel() {
        return debug;
    }

    public void updateDebugInfo(boolean debugMode) {
        if (debugMode) {
            debug.setText("ID:" + id + " X:" + x + " Y:" + y);
            debug.setBounds(x - 10, y - 20, 100, 15);
            debug.setVisible(true);
        } else {
            debug.setVisible(false);
        }
    }

    @Override
    public void run() {
        while (alive) {
            if (!alive)
                break;

            // เคลื่อนที่อุกกาบาตตามความเร็ว
            x += dx;
            y += dy;

            // จำกัดความเร็ว
            if (Math.abs(dx) > Config.MAX_SPEED) {
                dx = dx > 0 ? Config.MAX_SPEED : -Config.MAX_SPEED;
            }
            if (Math.abs(dy) > Config.MAX_SPEED) {
                dy = dy > 0 ? Config.MAX_SPEED : -Config.MAX_SPEED;
            }

            // ชนขอบซ้าย
            if (x <= 0) {
                dx = (int) (-dx * Config.BOUNCE_MULTIPLIER); // เปลี่ยนทิศทางและคูณความเร็ว
                dy += (int) ((Math.random() - 0.5) * Config.BOUNCE_RANDOM_RANGE);
                x = 0; // ตั้งตำแหน่งให้อยู่ที่ขอบ
                SoundManager.playDrip();
            }

            // ชนขอบขวา
            if (x >= Config.WINDOW_WIDTH - Config.ASTEROID_SIZE) {
                dx = (int) (-dx * Config.BOUNCE_MULTIPLIER); // เปลี่ยนทิศทางและคูณความเร็ว
                dy += (int) ((Math.random() - 0.5) * Config.BOUNCE_RANDOM_RANGE);
                x = Config.WINDOW_WIDTH - Config.ASTEROID_SIZE; // ตั้งตำแหน่งให้อยู่ที่ขอบ
                SoundManager.playDrip();
            }

            // ชนขอบบน
            if (y <= 0) {
                dy = (int) (-dy * Config.BOUNCE_MULTIPLIER); // เปลี่ยนทิศทางและคูณความเร็ว
                dx += (int) ((Math.random() - 0.5) * Config.BOUNCE_RANDOM_RANGE);
                y = 0;
                SoundManager.playDrip();
            }

            // ชนขอบล่าง
            if (y >= Config.WINDOW_HEIGHT - Config.ASTEROID_SIZE - 30) {
                dy = (int) (-dy * Config.BOUNCE_MULTIPLIER); // เปลี่ยนทิศทางและคูณความเร็ว
                dx += (int) ((Math.random() - 0.5) * Config.BOUNCE_RANDOM_RANGE);
                y = Config.WINDOW_HEIGHT - Config.ASTEROID_SIZE - 30;
                SoundManager.playDrip();
            }

            label.setLocation(x, y);
            if (debug != null) {
                debug.setBounds(x - 10, y - 20, 100, 15);
            }
            checkCollision();

            try {
                Thread.sleep(Config.GAME_SPEED);
            } catch (InterruptedException e) {
                break;
            }
        }
    }

    // ตรวจสอบการชนกันระหว่างอุกกาบาต
    private void checkCollision() {
        for (Asteroid other : App.getAsteroids()) {

            if (other == this || !other.isAlive()) {
                continue;
            }

            // คำนวณระยะห่างระหว่างอุกกาบาต
            double distance = Math.sqrt(Math.pow(x - other.x, 2) + Math.pow(y - other.y, 2));

            if (distance < Config.COLLISION_DISTANCE) {
                if (this.id < other.id) {
                    this.explode();
                } else {
                    other.explode();
                }
                break;
            }
        }
    }
}
