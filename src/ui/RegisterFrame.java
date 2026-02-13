package ui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.BorderLayout;

import javax.swing.*;

import dao.UserDao;
import model.User;

public class RegisterFrame extends JFrame {

    private JTextField emailField;
    private JPasswordField passwordField;
    private JComboBox<String> roleComboBox; 

    private UserDao userDao;

    public RegisterFrame(UserDao userDao) {
        this.userDao = userDao;
        init();
    }
    
    private void init() {
        setTitle("Register");
        setSize(1100, 650);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        JPanel rightPanel = new JPanel() {
            Image bg = new ImageIcon(
                    RegisterFrame.class.getResource("/image/register.jpg")
            ).getImage();

            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(bg, 0, 0, getWidth(), getHeight(), this);
            }
        };
        rightPanel.setLayout(new BorderLayout());

        JPanel leftPanel = new JPanel(new GridBagLayout());
        leftPanel.setBackground(new Color(245, 245, 245));
        leftPanel.setPreferredSize(new Dimension(480, 0)); 
        
        JPanel card = new JPanel();
        card.setPreferredSize(new Dimension(420, 500)); 
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));

        JLabel title = new JLabel("CREATE NEW ACCOUNT");
        title.setFont(new Font("Segoe UI", Font.BOLD, 26));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        emailField = new JTextField();
        passwordField = new JPasswordField();
        
        String[] roles = { "USER", "ADMIN" };
        roleComboBox = new JComboBox<>(roles);
        styleComboBox(roleComboBox);

        styleField(emailField);
        styleField(passwordField);

        JButton registerBtn = new JButton("Register");
        styleButton(registerBtn);
        registerBtn.addActionListener(e -> handleRegister());

        card.add(title);
        card.add(Box.createVerticalStrut(25));
        
        card.add(new JLabel("Email"));
        card.add(emailField);
        card.add(Box.createVerticalStrut(15));
        
        card.add(new JLabel("Parolă"));
        card.add(passwordField);
        card.add(Box.createVerticalStrut(15));
        
        card.add(new JLabel("Tip Cont (Rol)"));
        card.add(roleComboBox);
        
        card.add(Box.createVerticalStrut(30));
        card.add(registerBtn);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        gbc.insets = new Insets(80, 60, 0, 0);
        leftPanel.add(card, gbc);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(leftPanel, BorderLayout.WEST);
        mainPanel.add(rightPanel, BorderLayout.CENTER);

        setContentPane(mainPanel);

        revalidate();
        repaint();
    }

    private void styleField(JTextField field) {
        field.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200)),
                BorderFactory.createEmptyBorder(8, 10, 8, 10)
        ));
    }
    
    private void styleComboBox(JComboBox<String> combo) {
        combo.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        combo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        combo.setBackground(Color.WHITE);
        combo.setAlignmentX(Component.LEFT_ALIGNMENT);
    }

    private void styleButton(JButton btn) {
        btn.setFocusPainted(false);
        btn.setBackground(new Color(40, 40, 40));
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);

    }
    
    private void handleRegister() {
        String email = emailField.getText().trim();
        String password = new String(passwordField.getPassword()).trim();
        String selectedRole = (String) roleComboBox.getSelectedItem(); // Rolul ales din JComboBox

        if (email.isEmpty() || password.isEmpty()) {
            ToastDialog.show(this, "Date lipsă", "Vă rugăm să completați toate câmpurile pentru înregistrare!");
            return;
        }

        if (userDao.findByEmail(email) != null) {
            ToastDialog.show(this, "Eroare cont", "Există deja un cont creat cu această adresă de email!");
            return;
        }

        try {
            User user = new User(0, email, email, password, selectedRole);
            userDao.save(user);

            ToastDialog.show(this, "Cont creat", "Contul de " + selectedRole + " a fost creat cu succes!");
            this.dispose(); 
        } catch (Exception ex) {
            ToastDialog.show(this, "Eroare sistem", "A apărut o eroare la salvarea datelor în baza de date.");
            ex.printStackTrace();
        }
    }
}