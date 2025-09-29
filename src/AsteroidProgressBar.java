import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.swing.*;

public class AsteroidProgressBar extends JComponent {
    private BufferedImage barBg, barFill, barBorder;
    private int asteroidCount = 0;
    private int maxAsteroids = 100; 

    public AsteroidProgressBar() {
        try {
            Image barBgImage = Toolkit.getDefaultToolkit().createImage(System.getProperty("user.dir") + File.separator + "assets" + File.separator + "ui" + File.separator + "BarV7_Bar.png");
            Image barFillImage = Toolkit.getDefaultToolkit().createImage(System.getProperty("user.dir") + File.separator + "assets" + File.separator + "ui" + File.separator + "BarV7_ProgressBar.png");
            Image barBorderImage = Toolkit.getDefaultToolkit().createImage(System.getProperty("user.dir") + File.separator + "assets" + File.separator + "ui" + File.separator + "BarV7_ProgressBarBorder.png");
            
            MediaTracker tracker = new MediaTracker(this);
            tracker.addImage(barBgImage, 0);
            tracker.addImage(barFillImage, 1);
            tracker.addImage(barBorderImage, 2);
            
            try {
                tracker.waitForAll();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            
            barBg = new BufferedImage(barBgImage.getWidth(null), barBgImage.getHeight(null), BufferedImage.TYPE_INT_ARGB);
            barFill = new BufferedImage(barFillImage.getWidth(null), barFillImage.getHeight(null), BufferedImage.TYPE_INT_ARGB);
            barBorder = new BufferedImage(barBorderImage.getWidth(null), barBorderImage.getHeight(null), BufferedImage.TYPE_INT_ARGB);
            
            Graphics2D g2d = barBg.createGraphics();
            g2d.drawImage(barBgImage, 0, 0, null);
            g2d.dispose();
            
            g2d = barFill.createGraphics();
            g2d.drawImage(barFillImage, 0, 0, null);
            g2d.dispose();
            
            g2d = barBorder.createGraphics();
            g2d.drawImage(barBorderImage, 0, 0, null);
            g2d.dispose();
        } catch (Exception e) {
            barBg = new BufferedImage(300, 50, BufferedImage.TYPE_INT_ARGB);
            barFill = new BufferedImage(300, 50, BufferedImage.TYPE_INT_ARGB);
            barBorder = new BufferedImage(300, 50, BufferedImage.TYPE_INT_ARGB);
            
            Graphics2D g2d = barBg.createGraphics();
            g2d.setColor(Color.DARK_GRAY);
            g2d.fillRect(0, 0, 300, 50);
            g2d.dispose();
            
            g2d = barFill.createGraphics();
            g2d.setColor(Color.GREEN);
            g2d.fillRect(0, 0, 300, 50);
            g2d.dispose();
            
            g2d = barBorder.createGraphics();
            g2d.setColor(Color.WHITE);
            g2d.drawRect(0, 0, 299, 49);
            g2d.dispose();
        }
        setPreferredSize(new Dimension(300, 50));
        setOpaque(false);
    }

    public void setAsteroidCount(int value) {
        int newCount = Math.max(0, Math.min(maxAsteroids, value));
        if (newCount != asteroidCount) {
            asteroidCount = newCount;
            SwingUtilities.invokeLater(this::repaint);
        }
    }

    public void setMaxAsteroids(int max) {
        this.maxAsteroids = max;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        int width = getWidth();
        int height = getHeight();
        
        if (barBg != null) {
            g.drawImage(barBg, 0, 0, width, height, null);
        }

        double percent = asteroidCount / (double) maxAsteroids;
        int fillWidth = (int) (width * percent);

        if (barFill != null) {
            g.drawImage(barFill,
                    0, 0, fillWidth, height,
                    0, 0, barFill.getWidth(), barFill.getHeight(),
                    null);
        }

        if (barBorder != null) {
            g.drawImage(barBorder, 0, 0, width, height, null);
        }

        g.setColor(Color.WHITE);
        g.setFont(new Font("Tahoma", Font.BOLD, 16));
        String text = "Asteroids: " + asteroidCount + " / " + maxAsteroids;
        FontMetrics fm = g.getFontMetrics();
        int textWidth = fm.stringWidth(text);
        int textX = (width - textWidth) / 2;
        int textY = height / 2 + fm.getAscent() / 2 - 5;
        g.drawString(text, textX, textY);
    }
}
