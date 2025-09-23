import java.awt.*;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.io.File;
import java.io.IOException;

public class CreditsWindow {
    private JFrame frame;

    public CreditsWindow() {
        frame = new JFrame("Credits - Asteroid Game");
        frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        frame.setSize(500, 400);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setUndecorated(true);

        setupCreditsUI();
    }

    private void setupCreditsUI() {
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.setBackground(Color.BLACK);

        JLabel titleLabel = new JLabel("CREDITS", JLabel.CENTER);
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        mainPanel.add(titleLabel, BorderLayout.NORTH);

        JPanel creditsPanel = new JPanel();
        creditsPanel.setLayout(new GridLayout(3, 1, 0, 20));
        creditsPanel.setBackground(Color.BLACK);
        creditsPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JPanel person1Panel = createPersonPanel("Wachirawit Wongsaeng", "67011212055", "Project Manager",
                "assets/team/member_king.jpg");
        creditsPanel.add(person1Panel);

        JPanel person2Panel = createPersonPanel("Chindanai Phuhatsuan", "67011212026", "Lead Developer",
                "assets/team/member2.jpg");
        creditsPanel.add(person2Panel);

        JPanel person3Panel = createPersonPanel("Seranee Punapo", "67011212143", "UI/UX Designer",
                "assets/team/member_mint.jpg");
        creditsPanel.add(person3Panel);

        mainPanel.add(creditsPanel, BorderLayout.CENTER);

        JLabel closeLabel = new JLabel("Press ESC to close", JLabel.CENTER);
        closeLabel.setForeground(Color.GRAY);
        closeLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        closeLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        mainPanel.add(closeLabel, BorderLayout.SOUTH);

        frame.add(mainPanel);

        frame.addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyPressed(java.awt.event.KeyEvent e) {
                if (e.getKeyCode() == java.awt.event.KeyEvent.VK_ESCAPE) {
                    frame.setVisible(false);
                }
            }
        });
        frame.setFocusable(true);
    }

    private JPanel createPersonPanel(String name, String id, String job, String imagePath) {
        JPanel personPanel = new JPanel();
        personPanel.setLayout(new BorderLayout(10, 0));
        personPanel.setBackground(Color.BLACK);
        personPanel.setBorder(BorderFactory.createLineBorder(Color.WHITE, 1));

        ImageIcon personIcon = null;
        try {
            Image originalImage = ImageIO.read(new File(imagePath));
            Image resizedImage = originalImage.getScaledInstance(80, 80, Image.SCALE_SMOOTH);
            personIcon = new ImageIcon(resizedImage);
        } catch (IOException e) {
            System.out.println("Cannot load image: " + imagePath);
            personIcon = new ImageIcon();
        }

        JLabel imageLabel = new JLabel(personIcon);
        imageLabel.setHorizontalAlignment(JLabel.CENTER);
        imageLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        personPanel.add(imageLabel, BorderLayout.WEST);

        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new GridLayout(3, 1, 0, 5));
        infoPanel.setBackground(Color.BLACK);
        infoPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel nameLabel = new JLabel(name);
        nameLabel.setForeground(Color.WHITE);
        nameLabel.setFont(new Font("Arial", Font.BOLD, 16));
        nameLabel.setHorizontalAlignment(JLabel.CENTER);

        JLabel idLabel = new JLabel("ID: " + id);
        idLabel.setForeground(Color.YELLOW);
        idLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        idLabel.setHorizontalAlignment(JLabel.CENTER);

        JLabel jobLabel = new JLabel(job);
        jobLabel.setForeground(Color.CYAN);
        jobLabel.setFont(new Font("Arial", Font.ITALIC, 14));
        jobLabel.setHorizontalAlignment(JLabel.CENTER);

        infoPanel.add(nameLabel);
        infoPanel.add(idLabel);
        infoPanel.add(jobLabel);

        personPanel.add(infoPanel, BorderLayout.CENTER);

        return personPanel;
    }

    public void show() {
        frame.setVisible(true);
        frame.requestFocus();
    }

    public void hide() {
        frame.setVisible(false);
    }

    public boolean isVisible() {
        return frame.isVisible();
    }
}
