package ui;

import javax.swing.*;
import java.awt.*;
import service.AuthService;
import service.SubscriptionService;
import dao.UserDao;
import model.User;

public class LoginFrame extends JFrame {

    private JTextField emailField;
    private JPasswordField passwordField;

    private final AuthService authService;
    private final SubscriptionService subscriptionService;
    private final UserDao userDao;
    private final String roleRestriction;
    
    public LoginFrame(AuthService authService,
                      SubscriptionService subscriptionService,
                      UserDao userDao,
                      String roleRestriction) {
        this.authService = authService;
        this.subscriptionService = subscriptionService;
        this.userDao = userDao;
        this.roleRestriction = roleRestriction;
        init();
    }

    private void init() {
        setTitle("Login - " + roleRestriction);
        setSize(1100, 650);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        
        JPanel background = new JPanel() {
            Image bg = new ImageIcon(LoginFrame.class.getResource("/image/login.jpg")).getImage();
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(bg, 0, 0, getWidth(), getHeight(), this);
            }
        };
        background.setLayout(new GridBagLayout());

        JPanel card = new JPanel();
        card.setPreferredSize(new Dimension(420, 420));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));

        JLabel title = new JLabel("Log in " + roleRestriction);
        title.setFont(new Font("Segoe UI", Font.BOLD, 26));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        emailField = new JTextField();
        passwordField = new JPasswordField();
        styleField(emailField);
        styleField(passwordField);

        JButton loginBtn = new JButton("Login");
        JButton registerBtn = new JButton("Register");
        styleButton(loginBtn);
        styleButton(registerBtn);

        loginBtn.addActionListener(e -> handleLogin());
        registerBtn.addActionListener(e -> openRegister());

        card.add(title);
        card.add(Box.createVerticalStrut(30));
        card.add(new JLabel("Email"));
        card.add(emailField);
        card.add(Box.createVerticalStrut(15));
        card.add(new JLabel("Parolă"));
        card.add(passwordField);
        card.add(Box.createVerticalStrut(30));
        card.add(loginBtn);
        card.add(Box.createVerticalStrut(10));
        card.add(registerBtn);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.CENTER;
        background.add(card, gbc);
        setContentPane(background);
    }

    private void handleLogin() {
        String email = emailField.getText().trim();
        String password = new String(passwordField.getPassword());

        if (email.isEmpty() || password.isEmpty()) {
            ToastDialog.show(this, "Câmpuri incomplete", "Vă rugăm să completați email-ul și parola!");
            return;
        }

        User user = authService.authenticate(email, password);

        if (user != null) {
            String userRole = user.getRole().trim().toUpperCase();
            
            if (roleRestriction != null && !roleRestriction.isEmpty()) {
                if (!userRole.contains(roleRestriction.toUpperCase())) {
                    ToastDialog.show(this, "Acces Respins", 
                        "Contul are rol de " + userRole + ", dar ați selectat logare ca " + roleRestriction);
                    return;
                }
            }

            System.out.println("LOGARE REUȘITĂ! Deschidere interfață pentru: " + userRole);

            if (userRole.contains("ADMIN")) {
                new AdminFrame(user, subscriptionService).setVisible(true);
            } else {
                new MainFrame(user, subscriptionService, userDao).setVisible(true);
            }
            
            this.dispose(); 
        } else {
            ToastDialog.show(this, "Autentificare eșuată", "Email-ul sau parola sunt greșite!");
        }
    }

    private void styleField(JTextField field) {
        field.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200)),
                BorderFactory.createEmptyBorder(8, 10, 8, 10)
        ));
    }

    private void styleButton(JButton btn) {
        btn.setFocusPainted(false);
        btn.setBackground(new Color(40, 40, 40));
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    private void openRegister() {
        new RegisterFrame(userDao).setVisible(true);
    }
}