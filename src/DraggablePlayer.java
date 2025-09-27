import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.*;

public class DraggablePlayer extends JLabel {
    private static final int SIZE = 64;
    private static final int START_X = Config.WINDOW_WIDTH / 2 - SIZE / 2;
    private static final int START_Y = Config.WINDOW_HEIGHT - 150;
    
    private boolean dragging = false;
    private final Point offset = new Point();
    private JLabel emoteLabel;
    
    public DraggablePlayer() {
        super();
        loadImage();
        setupDrag();
        createEmoteLabel();
        setSize(SIZE, SIZE);
        setLocation(START_X, START_Y);
        setOpaque(false);
    }
    
    private void loadImage() {
        try {
            BufferedImage image = ImageIO.read(new File("assets/images/PlayerBlue_Frame_01_png_processed.png"));
            if (image != null) {
                Image scaled = image.getScaledInstance(SIZE, SIZE, Image.SCALE_SMOOTH);
                setIcon(new ImageIcon(scaled));
            }
        } catch (IOException e) {
            setIcon(createDefaultIcon());
        }
    }
    
    private ImageIcon createDefaultIcon() {
        BufferedImage icon = new BufferedImage(SIZE, SIZE, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = icon.createGraphics();
        g.setColor(Color.BLUE);
        g.fillRect(0, 0, SIZE, SIZE);
        g.setColor(Color.WHITE);
        g.drawString("P", 28, 40);
        g.dispose();
        return new ImageIcon(icon);
    }
    
    private void createEmoteLabel() {
        emoteLabel = new JLabel();
        try {
            ImageIcon emoteIcon = new ImageIcon("assets/images/emote.gif");
            emoteLabel.setIcon(emoteIcon);
        } catch (Exception e) {
            System.out.println("ไม่สามารถโหลด emote.gif ได้");
        }
        emoteLabel.setOpaque(false);
        emoteLabel.setBounds(START_X + SIZE/2 - 16, START_Y - 40, 32, 32);
    }
    
    private void setupDrag() {
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (SwingUtilities.isLeftMouseButton(e)) {
                    dragging = true;
                    offset.x = e.getX();
                    offset.y = e.getY();
                    setCursor(Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR));
                }
            }
            
            @Override
            public void mouseReleased(MouseEvent e) {
                if (SwingUtilities.isLeftMouseButton(e)) {
                    dragging = false;
                    setCursor(Cursor.getDefaultCursor());
                }
            }
        });
        
        addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                if (dragging && SwingUtilities.isLeftMouseButton(e)) {
                    Point newPos = new Point(
                        getLocation().x + e.getX() - offset.x,
                        getLocation().y + e.getY() - offset.y
                    );
                    
                    Container parent = getParent();
                    if (parent != null) {
                        int maxX = parent.getWidth() - getWidth();
                        int maxY = parent.getHeight() - getHeight();
                        
                        newPos.x = Math.max(0, Math.min(newPos.x, maxX));
                        newPos.y = Math.max(0, Math.min(newPos.y, maxY));
                    }
                    
                    setLocation(newPos);
                    updateEmotePosition();
                }
            }
        });
    }
    
    public void setPosition(int x, int y) {
        setLocation(x, y);
    }
    
    public Point getPosition() {
        return getLocation();
    }
    
    public JLabel getEmoteLabel() {
        return emoteLabel;
    }
    
    private void updateEmotePosition() {
        if (emoteLabel != null) {
            emoteLabel.setLocation(getX() + SIZE/2 - 16, getY() - 40);
        }
    }
}