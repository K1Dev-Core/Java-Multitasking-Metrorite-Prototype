import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import javax.swing.*;

public class Asteroid implements Runnable {

    private final int id;
    private int x, y;
    private int dx, dy;
    private boolean alive = true;
    private boolean exploded = false;
    private final JLabel label;
    private final JPanel parent;
    private final ImageIcon icon;
    private final ImageIcon bomb;
    private final JLabel debug;

    public Asteroid(int id, int x, int y, int dx, int dy, JPanel parent) {
        this.id = id;
        this.x = x;
        this.y = y;
        this.dx = dx;
        this.dy = dy;
        this.parent = parent;

        int img = (int)(Math.random() * 13) + 1;
        String path = System.getProperty("user.dir") + File.separator + "assets" + File.separator + "images" + File.separator + img + ".gif";
        String bombPath = System.getProperty("user.dir") + File.separator + "assets" + File.separator + "images" + File.separator + "output-onlinegiftools.gif";

        Image iconImage = Toolkit.getDefaultToolkit().createImage(path);
        Image bombImage = Toolkit.getDefaultToolkit().createImage(bombPath);
        icon = new ImageIcon(iconImage);
        bomb = new ImageIcon(bombImage);
    


        this.label = new JLabel(icon);
        this.label.setBounds(x, y, Config.ASTEROID_SIZE, Config.ASTEROID_SIZE);
        this.label.setHorizontalAlignment(JLabel.CENTER);
        this.label.setVerticalAlignment(JLabel.CENTER);

        this.label.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                explode();
            }
        });

        debug = new JLabel();
        debug.setForeground(Color.YELLOW);
        debug.setFont(new Font("Tahoma", Font.PLAIN, 7));
        debug.setOpaque(false);
        debug.setVisible(false);
        this.parent.add(debug);
    }

    public void explode() {
        if (exploded) return;
        exploded = true;
        
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
                parent.remove(label);
                if (debug != null) {
                    parent.remove(debug);
                }
                parent.revalidate();
                parent.repaint();
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

    public void stop() {
        alive = false;
    }



    public void updateDebugInfo(boolean debugMode) {
        if (debugMode) {
            double speed = Math.sqrt(dx * dx + dy * dy);
            debug.setText("ID:" + id + " X:" + x + " Y:" + y + " S:" + String.format("%.1f", speed));
            debug.setBounds(x - 10, y - 20, 250, 15);
            debug.setVisible(true);
        } else {
            debug.setVisible(false);
        }
    }



    @Override
    public void run() {
        while (alive && !exploded) {
            x += dx;
            y += dy;

            if (Math.abs(dx) > Config.MAX_SPEED) {
                dx = dx > 0 ? Config.MAX_SPEED : -Config.MAX_SPEED;
            }
            if (Math.abs(dy) > Config.MAX_SPEED) {
                dy = dy > 0 ? Config.MAX_SPEED : -Config.MAX_SPEED;
            }

            if (x <= 0) {
                dx = (int)(Math.abs(dx) * Config.BOUNCE_MULTIPLIER);
                x = 0;
                SoundManager.playDrip();
            }

            if (x >= Config.WINDOW_WIDTH - Config.ASTEROID_SIZE) {
                dx = -(int)(Math.abs(dx) * Config.BOUNCE_MULTIPLIER);
                x = Config.WINDOW_WIDTH - Config.ASTEROID_SIZE;
                SoundManager.playDrip();
            }

            if (y <= 0) {
                dy = (int)(Math.abs(dy) * Config.BOUNCE_MULTIPLIER);
                y = 0;
                SoundManager.playDrip();
            }

            if (y >= Config.WINDOW_HEIGHT - Config.ASTEROID_SIZE - 60) {
                dy = -(int)(Math.abs(dy) * Config.BOUNCE_MULTIPLIER);
                y = Config.WINDOW_HEIGHT - Config.ASTEROID_SIZE - 60;
                SoundManager.playDrip();
            }

            label.setLocation(x, y);
            if (debug != null) {
                debug.setBounds(x - 10, y - 20, 250, 15);
            }
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
            if (other == this || !other.isAlive() || !this.isAlive() || this.exploded || other.exploded) {
                continue;
            }

            int deltaX = this.x - other.x;
            int deltaY = this.y - other.y;
            int distanceSquared = deltaX * deltaX + deltaY * deltaY;

            if (distanceSquared < Config.COLLISION_DISTANCE * Config.COLLISION_DISTANCE) {
               
                if (!this.exploded && !other.exploded) {
                    if (this.id < other.id) {
                        this.explode();
                    } else {
                        other.explode();
                    }
                }
                break;
            }
        }
    }
}
