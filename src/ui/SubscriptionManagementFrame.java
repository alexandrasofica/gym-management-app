package ui;

import model.Subscription;
import model.SubscriptionType;
import model.UserType;
import service.SubscriptionService;
import ui.ModernDialog;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class SubscriptionManagementFrame extends JFrame {

    private final SubscriptionService subscriptionService;

    private JTable subscriptionsTable;
    private DefaultTableModel tableModel;

    private JComboBox<String> userTypeComboBox;
    private JComboBox<String> subTypeComboBox;
    private JButton applyFiltersBtn;
    private JButton resetFiltersBtn;

    private JButton addBtn;
    private JButton deleteBtn;
    private JButton homeBtn;
    private JButton closeBtn;

    public SubscriptionManagementFrame(SubscriptionService subscriptionService) {
        this.subscriptionService = subscriptionService;

        initComponents();
        initUI();
        attachListeners();
        loadData(); 
    }

    public void reload() {
        loadData();
    }

    private void initComponents() {
        tableModel = new DefaultTableModel(
                new Object[]{"ID", "User ID", "Tip", "Tip utilizator", "Preț bază", "Preț final", "Start", "End", "Status"},
                0
        ) {
        	
        	/**
        	 * transforma tabelul in read-only
        	 */
            @Override public boolean isCellEditable(int row, int col) { return false; }
        };

        subscriptionsTable = new JTable(tableModel);

        userTypeComboBox = new JComboBox<>();
        subTypeComboBox = new JComboBox<>();
        applyFiltersBtn = new JButton("Aplică filtre");
        resetFiltersBtn = new JButton("Reset");

        userTypeComboBox.addItem("ALL");
        for (UserType ut : UserType.values()) userTypeComboBox.addItem(ut.name());

        subTypeComboBox.addItem("ALL");
        for (SubscriptionType st : SubscriptionType.values()) subTypeComboBox.addItem(st.name());

        addBtn = new JButton("Adaugă abonament");
        deleteBtn = new JButton("Șterge abonament");
        homeBtn = new JButton("Home");
        closeBtn = new JButton("Închide");
    }

    private void initUI() {
        setTitle("Gestionare abonamente");
        setSize(1100, 650);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        JPanel background = createBackgroundPanel("/image/login.jpg");
        background.setLayout(new GridBagLayout());

        JPanel card = new JPanel(new BorderLayout(15, 15));
        card.setPreferredSize(new Dimension(1000, 560));
        card.setBackground(new Color(0, 0, 0, 170));
        card.setBorder(new EmptyBorder(20, 20, 20, 20));

        JLabel title = new JLabel("GESTIONARE ABONAMENTE");
        title.setForeground(Color.WHITE);
        title.setFont(new Font("Segoe UI", Font.BOLD, 30));
        title.setHorizontalAlignment(SwingConstants.CENTER);

        JPanel header = new JPanel(new BorderLayout(0, 10));
        header.setOpaque(false);
        header.add(title, BorderLayout.NORTH);

        JPanel filters = new JPanel(new FlowLayout(FlowLayout.CENTER, 12, 5));
        filters.setOpaque(false);

        JLabel utLbl = new JLabel("Tip utilizator:");
        utLbl.setForeground(Color.WHITE);
        utLbl.setFont(new Font("Segoe UI", Font.BOLD, 13));

        JLabel stLbl = new JLabel("Tip abonament:");
        stLbl.setForeground(Color.WHITE);
        stLbl.setFont(new Font("Segoe UI", Font.BOLD, 13));

        styleCombo(userTypeComboBox);
        styleCombo(subTypeComboBox);
        styleWhiteButton(applyFiltersBtn);
        styleWhiteButton(resetFiltersBtn);

        filters.add(utLbl);
        filters.add(userTypeComboBox);
        filters.add(stLbl);
        filters.add(subTypeComboBox);
        filters.add(applyFiltersBtn);
        filters.add(resetFiltersBtn);

        header.add(filters, BorderLayout.SOUTH);
        card.add(header, BorderLayout.NORTH);

        subscriptionsTable.setRowHeight(28);
        subscriptionsTable.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        subscriptionsTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        subscriptionsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JScrollPane scrollPane = new JScrollPane(subscriptionsTable);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getViewport().setBackground(Color.WHITE);

        JPanel tableWrapper = new JPanel(new BorderLayout());
        tableWrapper.setBackground(Color.WHITE);
        tableWrapper.setBorder(new EmptyBorder(10, 10, 10, 10));
        tableWrapper.add(scrollPane, BorderLayout.CENTER);

        card.add(tableWrapper, BorderLayout.CENTER);

        JPanel actions = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        actions.setOpaque(false);

        styleActionButton(addBtn);
        styleActionButton(deleteBtn);
        styleActionButton(homeBtn);
        styleActionButton(closeBtn);

        actions.add(addBtn);
        actions.add(deleteBtn);
        actions.add(homeBtn);
        actions.add(closeBtn);

        card.add(actions, BorderLayout.SOUTH);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0; gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.CENTER;
        background.add(card, gbc);

        setContentPane(background);
    }

    private void attachListeners() {
        addBtn.addActionListener(e ->
                new SubscriptionFormFrame(this, subscriptionService).setVisible(true)
        );

        deleteBtn.addActionListener(e -> {
            String input = ModernDialog.prompt(this, "Ștergere", "Introdu user ID pentru ștergere:");
            if (input == null) return;
            input = input.trim();
            if (input.isEmpty()) return;

            try {
                int userId = Integer.parseInt(input);

                boolean ok = ModernDialog.confirm(this, "Confirmare",
                        "Sigur vrei să ștergi TOATE abonamentele pentru user_id = " + userId + "?");
                if (!ok) return;

                subscriptionService.deleteByUserId(userId);
                reload();
                ModernDialog.info(this, "Succes", "Abonamentele utilizatorului au fost șterse.");

            } catch (NumberFormatException ex) {
                ModernDialog.error(this, "Eroare", "User ID trebuie să fie număr!");
            } catch (Exception ex) {
                ModernDialog.error(this, "Eroare", "Eroare la ștergere:\n" + ex.getMessage());
                ex.printStackTrace();
            }
        });

        applyFiltersBtn.addActionListener(e -> applyFilters());

        resetFiltersBtn.addActionListener(e -> {
            userTypeComboBox.setSelectedItem("ALL");
            subTypeComboBox.setSelectedItem("ALL");
            loadData();
        });

        homeBtn.addActionListener(e -> dispose());
        closeBtn.addActionListener(e -> dispose());
    }

    private void loadData() {
        try {
            tableModel.setRowCount(0);
            List<Subscription> list = subscriptionService.getAll();

            fillTable(list);

        } catch (Exception ex) {
            ToastDialog.show(this, "Eroare", "Eroare la încărcare abonamente:\n" + ex.getMessage());
            ex.printStackTrace();
        }
    }

    private void applyFilters() {
        try {
            String ut = (String) userTypeComboBox.getSelectedItem();
            String st = (String) subTypeComboBox.getSelectedItem();

            List<Subscription> all = subscriptionService.getAll();
            List<Subscription> filtered = new ArrayList<>();

            for (Subscription s : all) {
                boolean ok = true;

                if (ut != null && !"ALL".equals(ut)) {
                    ok = ok && s.getUserType() != null && s.getUserType().name().equals(ut);
                }
                if (st != null && !"ALL".equals(st)) {
                    ok = ok && s.getType() != null && s.getType().name().equals(st);
                }

                if (ok) filtered.add(s);
            }

            tableModel.setRowCount(0);
            fillTable(filtered);

        } catch (Exception ex) {
            ToastDialog.show(this, "Eroare", "Eroare la filtrare:\n" + ex.getMessage());
            ex.printStackTrace();
        }
    }

    private void fillTable(List<Subscription> list) {
        for (Subscription s : list) {
            tableModel.addRow(new Object[]{
                    s.getId(),
                    s.getUserId(),
                    s.getType(),
                    s.getUserType(),
                    s.getBasePrice(),
                    s.getFinalPrice(),
                    s.getStartDate(),
                    s.getEndDate(),
                    s.getStatus()
            });
        }
    }

    private void styleActionButton(JButton btn) {
        btn.setFocusPainted(false);
        btn.setRolloverEnabled(false);
        btn.setOpaque(true);
        btn.setContentAreaFilled(true);
        btn.setBackground(Color.WHITE);
        btn.setForeground(Color.BLACK);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setPreferredSize(new Dimension(180, 40));
        btn.setBorder(BorderFactory.createLineBorder(new Color(180, 180, 180)));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    }

    private void styleWhiteButton(JButton btn) {
        btn.setFocusPainted(false);
        btn.setRolloverEnabled(false);
        btn.setOpaque(true);
        btn.setContentAreaFilled(true);
        btn.setBackground(Color.WHITE);
        btn.setForeground(Color.BLACK);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setBorder(BorderFactory.createLineBorder(new Color(180, 180, 180)));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(110, 32));
    }

    private void styleCombo(JComboBox<?> combo) {
        combo.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        combo.setPreferredSize(new Dimension(160, 30));
        combo.setBackground(Color.WHITE);
    }

    private JPanel createBackgroundPanel(String resourcePath) {
        URL u = getClass().getResource(resourcePath);
        if (u == null) {
            JPanel p = new JPanel();
            p.setBackground(new Color(20,20,20));
            return p;
        }

        Image img = new ImageIcon(u).getImage();

        return new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(img, 0, 0, getWidth(), getHeight(), this);
            }
        };
    }
}
