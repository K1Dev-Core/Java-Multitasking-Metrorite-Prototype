import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;
import javax.swing.*;

public class AsteroidProgressBar extends JComponent {
    private BufferedImage barBg, barFill, barBorder;
    private int asteroidCount = 0;
    private int maxAsteroids = 100; 

    public AsteroidProgressBar() {
        try {
            barBg = ImageIO.read(new File("assets/ui/BarV7_Bar.png"));
            barFill = ImageIO.read(new File("assets/ui/BarV7_ProgressBar.png"));
            barBorder = ImageIO.read(new File("assets/ui/BarV7_ProgressBarBorder.png"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        setPreferredSize(new Dimension(300, 50));
        setOpaque(false);
    }

    public void setAsteroidCount(int value) {
        int newCount = Math.max(0, Math.min(maxAsteroids, value));
        if (newCount != asteroidCount) {
            asteroidCount = newCount;
            repaint();
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
        
        g.drawImage(barBg, 0, 0, width, height, null);

        double percent = asteroidCount / (double) maxAsteroids;
        int fillWidth = (int) (width * percent);

        g.drawImage(barFill,
                0, 0, fillWidth, height,
                0, 0, barFill.getWidth(), barFill.getHeight(),
                null);

        g.drawImage(barBorder, 0, 0, width, height, null);

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
