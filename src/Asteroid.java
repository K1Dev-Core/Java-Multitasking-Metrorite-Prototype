import java.awt.*;
import javax.imageio.ImageIO;
import javax.swing.*;

// implements Runnable  thread แยก
public class Asteroid implements Runnable {

    private int id;
    private int x, y;
    private int dx, dy;
    private boolean alive = true;
    private JLabel label;
    private JPanel parentPanel;
    private ImageIcon asteroidIcon;
    private ImageIcon bombIcon;

    public Asteroid(int id, int x, int y, int dx, int dy, JPanel parent) {
        this.id = id;
        this.x = x;
        this.y = y;
        this.dx = dx;
        this.dy = dy;
        this.parentPanel = parent;

        try {

            int imageNum = (id % 10) + 1;
            String asteroidPath = "./images/" + imageNum + ".png";
            String bombPath = "./images/bomb.gif";

            asteroidIcon = new ImageIcon(ImageIO.read(new java.io.File(asteroidPath)));

            bombIcon = new ImageIcon(ImageIO.read(new java.io.File(bombPath)));

            Image asteroidImg = asteroidIcon.getImage().getScaledInstance(60, 60, Image.SCALE_SMOOTH);
            asteroidIcon = new ImageIcon(asteroidImg);

            bombIcon = new ImageIcon(bombPath);

        } catch (Exception e) {

            System.out.println("Cannot load image: " + e.getMessage());
            asteroidIcon = new ImageIcon();
            bombIcon = new ImageIcon();
        }

        this.label = new JLabel(asteroidIcon);

        this.label.setBounds(x, y, Config.ASTEROID_SIZE, Config.ASTEROID_SIZE);
    }

    // ฟังก์ชันระเบิด
    public void explode() {
        if (bombIcon != null) {

            label.setIcon(bombIcon);

            int offset = (Config.BOMB_SIZE - Config.ASTEROID_SIZE) / 2;
            label.setBounds(x - offset, y - offset, Config.BOMB_SIZE, Config.BOMB_SIZE);
        }
        alive = false;

        Timer explosionTimer = new Timer(Config.BOMB_DURATION, e -> {
            parentPanel.remove(label);
            parentPanel.repaint();
        });
        explosionTimer.setRepeats(false);
        explosionTimer.start();
    }

    public JLabel getLabel() {
        return label; // ส่ง label
    }

    public boolean isAlive() {
        return alive;
    }

    // thread แยก
    @Override
    public void run() {
        while (alive) {

            if (!alive)
                break;

            x += dx;
            y += dy;

            if (Math.abs(dx) > Config.MAX_SPEED)
                dx = dx > 0 ? Config.MAX_SPEED : -Config.MAX_SPEED;
            if (Math.abs(dy) > Config.MAX_SPEED)
                dy = dy > 0 ? Config.MAX_SPEED : -Config.MAX_SPEED;

            // ตรวจสอบการชนขอบหน้าจอ - เด้งกลับ
            if (x <= 0) {
                // ชนขอบซ้าย - เปลี่ยนทิศทางและเพิ่มความเร็ว
                dx = (int) (-dx * Config.BOUNCE_MULTIPLIER);
                x = 0;
            }
            if (x >= Config.WINDOW_WIDTH - Config.ASTEROID_SIZE - 5) {
                // ชนขอบขวา
                dx = (int) (-dx * Config.BOUNCE_MULTIPLIER);
                x = Config.WINDOW_WIDTH - Config.ASTEROID_SIZE - 5;
            }
            if (y <= 0) {
                // ชนขอบบน
                dy = (int) (-dy * Config.BOUNCE_MULTIPLIER);
                y = 0;
            }
            if (y >= Config.WINDOW_HEIGHT - Config.ASTEROID_SIZE - 5) {
                // ชนขอบล่าง
                dy = (int) (-dy * Config.BOUNCE_MULTIPLIER);
                y = Config.WINDOW_HEIGHT - Config.ASTEROID_SIZE - 5;
            }

            label.setLocation(x, y);

            // ตรวจสอบการชนกับกับอุกบาตอันอื่น
            checkCollision();

            try {
                Thread.sleep(Config.GAME_SPEED);
            } catch (InterruptedException e) {
                break;
            }
        }
    }

    private void checkCollision() {

        for (Asteroid other : App.getAsteroids()) {

            if (other == this || !other.isAlive())
                continue;

            // คำนวณระยะห่างระหว่าง อุกบาต
            double distance = Math.sqrt(Math.pow(x - other.x, 2) + Math.pow(y - other.y, 2));
            if (distance < Config.COLLISION_DISTANCE) {
                // ไอดีน้อยกว่าจะระเบิด
                // หรืออยากให้สุ่ม 50 -50
                // if (Math.random() < 0.5) {
                // if (this.id < other.id) {
                // this.explode();
                // } else {
                // other.explode();
                // }
                // }

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
