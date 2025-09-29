import java.awt.*;
import java.io.File;
import javax.swing.*;

public class CreditsWindow {
    private final JFrame frame;

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

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout());
        buttonPanel.setBackground(Color.BLACK);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));

        JButton exitButton = new JButton("Exit");
        exitButton.setFont(new Font("Arial", Font.BOLD, 14));
        exitButton.setPreferredSize(new Dimension(100, 35));
        exitButton.setOpaque(false);
        exitButton.setContentAreaFilled(false);
        exitButton.setBorderPainted(true);
        exitButton.setBorder(BorderFactory.createLineBorder(Color.WHITE, 2));
        exitButton.setForeground(Color.WHITE);
        exitButton.addActionListener(_ -> System.exit(0));

        JButton continueButton = new JButton("Continue");
        continueButton.setFont(new Font("Arial", Font.BOLD, 14));
        continueButton.setPreferredSize(new Dimension(100, 35));
        continueButton.setOpaque(false);
        continueButton.setContentAreaFilled(false);
        continueButton.setBorderPainted(true);
        continueButton.setBorder(BorderFactory.createLineBorder(Color.WHITE, 2));
        continueButton.setForeground(Color.WHITE);
        continueButton.addActionListener(_ -> frame.setVisible(false));

        buttonPanel.add(exitButton);
        buttonPanel.add(continueButton);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        frame.add(mainPanel);

        frame.setFocusable(true);
    }

    private JPanel createPersonPanel(String name, String id, String job, String imagePath) {
        JPanel personPanel = new JPanel();
        personPanel.setLayout(new BorderLayout(10, 0));
        personPanel.setBackground(Color.BLACK);
        personPanel.setBorder(BorderFactory.createLineBorder(Color.WHITE, 1));

        ImageIcon personIcon = null;
        try {
            String fullPath = System.getProperty("user.dir") + File.separator + imagePath;
            Image originalImage = Toolkit.getDefaultToolkit().createImage(fullPath);
            Image resizedImage = originalImage.getScaledInstance(80, 80, Image.SCALE_SMOOTH);
            personIcon = new ImageIcon(resizedImage);
        } catch (Exception e) {
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
