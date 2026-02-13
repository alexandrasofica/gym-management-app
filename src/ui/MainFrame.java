package ui;

import dao.UserDao;
import model.User;
import service.SubscriptionService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class MainFrame extends JFrame {

    private final User loggedUser;
    private final SubscriptionService subscriptionService;
    private final UserDao userDao;

    public MainFrame(User loggedUser, SubscriptionService subscriptionService, UserDao userDao) {
        this.loggedUser = loggedUser;
        this.subscriptionService = subscriptionService;
        this.userDao = userDao;
        init();
    }

    private void init() {
    	System.out.println("DEBUG: Rolul detectat în MainFrame este: " + loggedUser.getRole());
        setTitle("Meniu principal - Gym Management");
        setSize(1100, 650);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JPanel background = new JPanel() {
            private final Image bg = new ImageIcon(
                    MainFrame.class.getResource("/image/login.jpg") 
            ).getImage();

            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(bg, 0, 0, getWidth(), getHeight(), this);
            }
        };
        background.setLayout(new GridBagLayout());

        JPanel card = new JPanel();
        card.setPreferredSize(new Dimension(600, 500));
        card.setBackground(new Color(0, 0, 0, 160));
        card.setLayout(new BorderLayout(0, 18));
        card.setBorder(new EmptyBorder(25, 25, 25, 25));

        String email = (loggedUser != null && loggedUser.getEmail() != null) ? loggedUser.getEmail() : "Oaspeți";
        String role = (loggedUser != null) ? loggedUser.getRole() : "USER";
        
        JLabel title = new JLabel("BUN VENIT, " + email + " (" + role + ")!");
        title.setHorizontalAlignment(SwingConstants.CENTER);
        title.setForeground(Color.WHITE);
        title.setFont(new Font("Segoe UI", Font.BOLD, 28));
        card.add(title, BorderLayout.NORTH);

        JPanel grid = new JPanel(new GridLayout(0, 2, 14, 14));
        grid.setOpaque(false);

        if ("ADMIN".equalsIgnoreCase(role)) {
            grid.add(createMenuTile("GESTIONARE\nABONAMENTE", () ->
                    new SubscriptionManagementFrame(subscriptionService).setVisible(true)
            ));
            grid.add(createMenuTile("GESTIONARE\nREZERVĂRI", () ->
                    new ReservationManagementFrame().setVisible(true)
            ));
        } else {
 
            grid.add(createMenuTile("ABONAMENTE\nDISPONIBILE", () ->
                    new SubscriptionMenuFrame(subscriptionService, loggedUser).setVisible(true)
            ));
            grid.add(createMenuTile("REZERVARE", () ->
                    new ReservationManagementFrame().setVisible(true)
            ));
            grid.add(createMenuTile("PROFIL\nUTILIZATOR", () ->
                    new UserProfileFrame(loggedUser, userDao).setVisible(true)
            ));
            grid.add(createMenuTile("OBIECTIVE\nFITNESS", () ->
                    new FitnessGoalsFrame(loggedUser).setVisible(true)
            ));
            grid.add(createMenuTile("PLAN\nDIETĂ", () ->
                    new DietPlanFrame(loggedUser, this).setVisible(true)            
            ));
  
        }

        card.add(grid, BorderLayout.CENTER);

        JPanel logoutTile = createMenuTile("LOGOUT", this::dispose);
        logoutTile.setPreferredSize(new Dimension(200, 55));
        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        bottom.setOpaque(false);
        bottom.add(logoutTile);
        card.add(bottom, BorderLayout.SOUTH);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0; gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.CENTER;
        background.add(card, gbc);

        setContentPane(background);
    }

    private JPanel createMenuTile(String text, Runnable onClick) {
        JPanel tile = new JPanel();
        tile.setLayout(new BoxLayout(tile, BoxLayout.Y_AXIS));
        tile.setBackground(new Color(0, 0, 0, 180));
        tile.setBorder(BorderFactory.createLineBorder(new Color(255, 255, 255, 60), 1));
        tile.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        tile.add(Box.createVerticalGlue());

        String[] lines = text.split("\n");
        for (String line : lines) {
            JLabel lbl = new JLabel(line);
            lbl.setForeground(Color.WHITE);
            lbl.setFont(new Font("Segoe UI", Font.BOLD, 16));
            lbl.setAlignmentX(Component.CENTER_ALIGNMENT);
            tile.add(lbl);
        }

        tile.add(Box.createVerticalGlue());
        tile.addMouseListener(new MouseAdapter() {
            @Override public void mouseClicked(MouseEvent e) { onClick.run(); }
        });
        return tile;
    }
}