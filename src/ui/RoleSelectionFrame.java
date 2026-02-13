package ui;

import dao.UserDaoJdbcImpl;
import service.AuthService;
import service.SubscriptionService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class RoleSelectionFrame extends JFrame {

    private final AuthService authService;
    private final SubscriptionService subscriptionService;
    private final UserDaoJdbcImpl userDao;
    private String roleRestriction; 

    public RoleSelectionFrame(AuthService authService, 
                            SubscriptionService subscriptionService, 
                            UserDaoJdbcImpl userDao, 
                            String roleRestriction) {
        this.authService = authService;
        this.subscriptionService = subscriptionService;
        this.userDao = userDao;
        this.roleRestriction = roleRestriction;
        
        setTitle("Gym Management - Selectează Acces");
        setSize(1100, 650);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        init();
    }
    
    private void init() {
        JPanel background = new JPanel() {
            private final Image bg = new ImageIcon(getClass().getResource("/image/login.jpg")).getImage();
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(bg, 0, 0, getWidth(), getHeight(), this);
            }
        };
        background.setLayout(new GridBagLayout());

        JPanel card = new JPanel();
        card.setPreferredSize(new Dimension(350, 250));
        card.setBackground(new Color(0, 0, 0, 180));
        card.setLayout(new GridLayout(3, 1, 10, 20));
        card.setBorder(new EmptyBorder(20, 20, 20, 20));

        JLabel infoLabel = new JLabel("CINE SUNTEȚI?");
        infoLabel.setForeground(Color.WHITE);
        infoLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
        infoLabel.setHorizontalAlignment(SwingConstants.CENTER);
        card.add(infoLabel);

        JButton adminBtn = createStyledButton("ADMINISTRATOR");
        JButton userBtn = createStyledButton("CLIENT (USER)");
        adminBtn.addActionListener(e -> openLogin("ADMIN"));
        userBtn.addActionListener(e -> openLogin("USER"));

        card.add(adminBtn);
        card.add(userBtn);

        background.add(card);
        setContentPane(background);
    }

    private JButton createStyledButton(String text) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setBackground(new Color(60, 63, 65));
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }

    private void openLogin(String selectedRole) {
        new LoginFrame(authService, subscriptionService, userDao, selectedRole).setVisible(true); 
        this.dispose();
    }
}