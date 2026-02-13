package ui;

import model.SubscriptionType;
import model.User;
import model.UserType;
import service.SubscriptionService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class SubscriptionMenuFrame extends JFrame {

    private final SubscriptionService subscriptionService;
    private final User loggedUser;

    public SubscriptionMenuFrame(SubscriptionService subscriptionService, User loggedUser) {
        this.subscriptionService = subscriptionService;
        this.loggedUser = loggedUser;
        init();
    }

    private void init() {
        setTitle("Abonamente disponibile");
        setSize(1100, 650);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        JPanel background = new JPanel() {
            private final Image bg = new ImageIcon(
                    SubscriptionMenuFrame.class.getResource("/image/login.jpg") // schimbă dacă vrei alt bg
            ).getImage();

            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(bg, 0, 0, getWidth(), getHeight(), this);
            }
        };
        background.setLayout(new BorderLayout());

        JPanel container = new JPanel(new BorderLayout(0, 12));
        container.setOpaque(false);
        container.setBorder(new EmptyBorder(30, 60, 30, 60));

        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);

        JLabel title = new JLabel("ABONAMENTE DISPONIBILE");
        title.setForeground(Color.WHITE);
        title.setFont(new Font("Segoe UI", Font.BOLD, 46));

        JButton homeBtn = new JButton("Home");
        styleWhiteButton(homeBtn);
        homeBtn.setPreferredSize(new Dimension(140, 55));
        homeBtn.addActionListener(e -> dispose());

        header.add(title, BorderLayout.WEST);
        header.add(homeBtn, BorderLayout.EAST);

        JPanel listPanel = new JPanel();
        listPanel.setOpaque(false);
        listPanel.setLayout(new BoxLayout(listPanel, BoxLayout.Y_AXIS));

        buildCatalog(listPanel); 

        JScrollPane scroll = new JScrollPane(listPanel);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        scroll.setOpaque(false);
        scroll.getViewport().setOpaque(false);

        scroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scroll.getVerticalScrollBar().setUnitIncrement(16);

        container.add(header, BorderLayout.NORTH);
        container.add(scroll, BorderLayout.CENTER);

        background.add(container, BorderLayout.CENTER);
        setContentPane(background);
    }

    private void buildCatalog(JPanel listPanel) {
        listPanel.removeAll();

        addCatalogItem(listPanel, "Abonament LUNAR - NORMAL",  SubscriptionType.LUNAR, UserType.NORMAL, 200.0);
        addCatalogItem(listPanel, "Abonament LUNAR - STUDENT", SubscriptionType.LUNAR, UserType.STUDENT, 200.0);
        addCatalogItem(listPanel, "Abonament LUNAR - ANGAJAT", SubscriptionType.LUNAR, UserType.ANGAJAT, 200.0);

        addCatalogItem(listPanel, "Abonament ANUAL - NORMAL",  SubscriptionType.ANUAL, UserType.NORMAL, 900.0);
        addCatalogItem(listPanel, "Abonament ANUAL - STUDENT", SubscriptionType.ANUAL, UserType.STUDENT, 900.0);
        addCatalogItem(listPanel, "Abonament ANUAL - ANGAJAT", SubscriptionType.ANUAL, UserType.ANGAJAT, 900.0);

        listPanel.revalidate();
        listPanel.repaint();
    }

    private void addCatalogItem(JPanel listPanel,
                                String titleText,
                                SubscriptionType type,
                                UserType userType,
                                double basePrice) {

        JPanel row = new JPanel(new BorderLayout(20, 0));
        row.setOpaque(false);
        row.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(255, 255, 255, 35)));


        JPanel card = new JPanel(new BorderLayout(20, 0));
        card.setBackground(new Color(0, 0, 0, 170));
        card.setBorder(new EmptyBorder(18, 22, 18, 22));

        JPanel left = new JPanel();
        left.setOpaque(false);
        left.setLayout(new BoxLayout(left, BoxLayout.Y_AXIS));

        JLabel t = new JLabel(titleText);
        t.setForeground(Color.WHITE);
        t.setFont(new Font("Segoe UI", Font.BOLD, 28));

        JLabel l1 = new JLabel("Tip: " + type + " | Utilizator: " + userType);
        l1.setForeground(Color.WHITE);
        l1.setFont(new Font("Segoe UI", Font.PLAIN, 15));

        JLabel l2 = new JLabel("Preț bază: " + basePrice + " lei");
        l2.setForeground(Color.WHITE);
        l2.setFont(new Font("Segoe UI", Font.PLAIN, 15));

        left.add(t);
        left.add(Box.createVerticalStrut(8));
        left.add(l1);
        left.add(Box.createVerticalStrut(4));
        left.add(l2);

        JButton chooseBtn = new JButton("Alege");
        styleWhiteButton(chooseBtn);
        chooseBtn.setPreferredSize(new Dimension(160, 55));

        chooseBtn.addActionListener(e -> {
            int defaultUserId = (loggedUser != null) ? loggedUser.getId() : 0;

            SubscriptionAddDialog dialog = new SubscriptionAddDialog(
                    this,
                    subscriptionService,
                    defaultUserId,
                    type,
                    userType,
                    basePrice
            );
            dialog.setVisible(true);

        });

        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        right.setOpaque(false);
        right.add(chooseBtn);

        card.add(left, BorderLayout.CENTER);
        card.add(right, BorderLayout.EAST);

        row.add(card, BorderLayout.CENTER);
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 160));

        listPanel.add(row);
        listPanel.add(Box.createVerticalStrut(14));
    }

    private void styleWhiteButton(JButton btn) {
        btn.setFocusPainted(false);
        btn.setRolloverEnabled(false);      
        btn.setBorderPainted(false);
        btn.setContentAreaFilled(true);
        btn.setOpaque(true);

        btn.setBackground(Color.WHITE);
        btn.setForeground(Color.BLACK);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 18));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    }
}
