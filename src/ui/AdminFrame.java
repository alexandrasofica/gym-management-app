package ui;

import model.User;
import service.SubscriptionService;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
/**
 * Clasa reprezintă interfața grafică pentru Administrator
 */
public class AdminFrame extends JFrame {

    private final User loggedUser;
    private final SubscriptionService subscriptionService;
    /**
     * Constructor
     * @param loggedUser
     * @param subscriptionService
     */
    public AdminFrame(User loggedUser, SubscriptionService subscriptionService) {
        this.loggedUser = loggedUser;
        this.subscriptionService = subscriptionService;
        init();
    }
    /**
     * Construiește toată interfața grafică
     */
    private void init() {
        setTitle("PANOU CONTROL - Administrator");
        setSize(1100, 650);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JPanel background = new JPanel() {
            private final Image bg = new ImageIcon(
                    getClass().getResource("/image/login.jpg") 
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

        JLabel title = new JLabel("MOD ADMINISTRATOR: " + loggedUser.getEmail());
        title.setHorizontalAlignment(SwingConstants.CENTER);
        title.setForeground(new Color(255, 204, 0)); 
        title.setFont(new Font("Segoe UI", Font.BOLD, 28));
        card.add(title, BorderLayout.NORTH);

        JPanel grid = new JPanel(new GridLayout(1, 2, 20, 0)); 
        grid.setOpaque(false);

        grid.add(createMenuTile("GESTIONARE\nABONAMENTE", () ->
                new SubscriptionManagementFrame(subscriptionService).setVisible(true)
        ));
        grid.add(createMenuTile("GESTIONARE\nREZERVĂRI", () ->
                new ReservationManagementFrame().setVisible(true)
        ));

        card.add(grid, BorderLayout.CENTER);

        JPanel logoutTile = createMenuTile("LOGOUT", () -> {
            this.dispose();
        });
        logoutTile.setPreferredSize(new Dimension(200, 55));
        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        bottom.setOpaque(false);
        bottom.add(logoutTile);
        card.add(bottom, BorderLayout.SOUTH);

        background.add(card, new GridBagConstraints());
        setContentPane(background);
    }
    /**
     * Creează un "card" clickable pe care sunt puse butoanele
     * @param text
     * @param onClick
     * @return panoul grafic 
     */
    private JPanel createMenuTile(String text, Runnable onClick) {
        JPanel tile = new JPanel();
        tile.setLayout(new BoxLayout(tile, BoxLayout.Y_AXIS));
        
        tile.setBackground(new Color(20, 20, 20)); 
        tile.setBorder(BorderFactory.createLineBorder(new Color(255, 255, 255, 40), 1));
        tile.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        
        tile.add(Box.createVerticalGlue());

        String[] lines = text.split("\n");
        for (String line : lines) {
            JLabel lbl = new JLabel(line);
            lbl.setForeground(Color.WHITE);
            lbl.setFont(new Font("Segoe UI", Font.BOLD, 18));
            lbl.setAlignmentX(Component.CENTER_ALIGNMENT);
            tile.add(lbl);
        }

        tile.add(Box.createVerticalGlue());
        
     
        tile.addMouseListener(new MouseAdapter() {
        	/**
        	 * Metodă pentru schimbarea ferestrei
        	 * sau logout
        	 */
            @Override 
            public void mouseClicked(MouseEvent e) { 
                onClick.run(); 
            }
        });
        
        return tile;
    }
}