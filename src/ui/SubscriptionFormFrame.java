package ui;

import model.SubscriptionType;
import model.User;
import model.UserType;
import service.SubscriptionService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.time.format.DateTimeParseException;

public class SubscriptionFormFrame extends JFrame {

    private final SubscriptionManagementFrame parent;
    private final SubscriptionService subscriptionService;
    private final User loggedUser;

    private JTextField userIdField;
    private JComboBox<SubscriptionType> typeBox;
    private JComboBox<UserType> userTypeBox;
    private JTextField basePriceField;
    private JTextField startDateField;
    private JTextField endDateField;

    public SubscriptionFormFrame(SubscriptionManagementFrame parent,
                                 SubscriptionService subscriptionService,
                                 User loggedUser) {
        this.parent = parent;
        this.subscriptionService = subscriptionService;
        this.loggedUser = loggedUser;
        init();
    }

    public SubscriptionFormFrame(SubscriptionManagementFrame parent,
                                 SubscriptionService subscriptionService) {
        this(parent, subscriptionService, null);
    }

    private void init() {
        setTitle("Creare abonament");
        setSize(520, 420);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(new Color(20,20,20));

        JPanel card = new JPanel(new GridBagLayout());
        card.setBackground(new Color(0,0,0,170));
        card.setBorder(new EmptyBorder(18, 18, 18, 18));

        JLabel title = new JLabel("ADAUGĂ ABONAMENT");
        title.setForeground(Color.WHITE);
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));
        title.setHorizontalAlignment(SwingConstants.CENTER);

        JPanel top = new JPanel(new BorderLayout());
        top.setOpaque(false);
        top.setBorder(new EmptyBorder(14, 14, 6, 14));
        top.add(title, BorderLayout.CENTER);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        int row = 0;

        gbc.gridx = 0; gbc.gridy = row;
        card.add(label("User ID:"), gbc);

        userIdField = field(10);
        if (loggedUser != null) {
            userIdField.setText(String.valueOf(loggedUser.getId()));
            userIdField.setEditable(false);
        }
        gbc.gridx = 1;
        card.add(userIdField, gbc);

        row++;
        gbc.gridx = 0; gbc.gridy = row;
        card.add(label("Tip abonament:"), gbc);

        typeBox = new JComboBox<>(SubscriptionType.values());
        styleCombo(typeBox);
        gbc.gridx = 1;
        card.add(typeBox, gbc);

        row++;
        gbc.gridx = 0; gbc.gridy = row;
        card.add(label("Tip utilizator:"), gbc);

        userTypeBox = new JComboBox<>(UserType.values());
        styleCombo(userTypeBox);
        gbc.gridx = 1;
        card.add(userTypeBox, gbc);

        row++;
        gbc.gridx = 0; gbc.gridy = row;
        card.add(label("Preț bază:"), gbc);

        basePriceField = field(10);
        gbc.gridx = 1;
        card.add(basePriceField, gbc);

        row++;
        gbc.gridx = 0; gbc.gridy = row;
        card.add(label("Data start (yyyy-MM-dd):"), gbc);

        startDateField = field(10);
        gbc.gridx = 1;
        card.add(startDateField, gbc);

        row++;
        gbc.gridx = 0; gbc.gridy = row;
        card.add(label("Data final (yyyy-MM-dd):"), gbc);

        endDateField = field(10);
        gbc.gridx = 1;
        card.add(endDateField, gbc);

        JButton saveBtn = whiteBtn("Salvează");
        JButton cancelBtn = whiteBtn("Anulează");
        saveBtn.addActionListener(e -> handleSave());
        cancelBtn.addActionListener(e -> dispose());

        JPanel btns = new JPanel(new FlowLayout(FlowLayout.CENTER, 12, 10));
        btns.setOpaque(false);
        btns.add(saveBtn);
        btns.add(cancelBtn);

        JPanel centerWrap = new JPanel(new BorderLayout());
        centerWrap.setBackground(new Color(20,20,20));
        centerWrap.setBorder(new EmptyBorder(0, 18, 18, 18));
        centerWrap.add(card, BorderLayout.CENTER);
        centerWrap.add(btns, BorderLayout.SOUTH);

        root.add(top, BorderLayout.NORTH);
        root.add(centerWrap, BorderLayout.CENTER);

        setContentPane(root);
    }

    public void preset(SubscriptionType type, UserType userType, double basePrice) {
        typeBox.setSelectedItem(type);
        userTypeBox.setSelectedItem(userType);
        basePriceField.setText(String.valueOf(basePrice));
    }

    private void handleSave() {
        try {
            String startStr = startDateField.getText().trim();
            String endStr = endDateField.getText().trim();

            java.time.LocalDate dateStart = java.time.LocalDate.parse(startStr);
            java.time.LocalDate dateEnd = java.time.LocalDate.parse(endStr);

            if (dateStart.isAfter(dateEnd)) {

                ToastDialog.show(this, "Eroare Validare", 
                    "Data de început (" + startStr + ") nu poate fi mai mare " +
                    "decât data de expirare (" + endStr + ")!");
                return; 
            }         
        } catch (java.time.format.DateTimeParseException ex) {
            ToastDialog.show(this, "Format Incorect", "Folosiți formatul AAAA-LL-ZZ (ex: 2025-01-01)");
        }
    }

    private JLabel label(String t) {
        JLabel l = new JLabel(t);
        l.setForeground(Color.WHITE);
        l.setFont(new Font("Segoe UI", Font.BOLD, 13));
        return l;
    }

    private JTextField field(int cols) {
        JTextField f = new JTextField(cols);
        f.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        f.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(255,255,255,60), 1),
                new EmptyBorder(8, 10, 8, 10)
        ));
        return f;
    }

    private void styleCombo(JComboBox<?> combo) {
        combo.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        combo.setBackground(Color.WHITE);
        combo.setPreferredSize(new Dimension(200, 34));
    }

    private JButton whiteBtn(String text) {
        JButton b = new JButton(text);
        b.setFocusPainted(false);
        b.setRolloverEnabled(false);
        b.setOpaque(true);
        b.setContentAreaFilled(true);
        b.setBackground(Color.WHITE);
        b.setForeground(Color.BLACK);
        b.setFont(new Font("Segoe UI", Font.BOLD, 13));
        b.setBorder(BorderFactory.createLineBorder(new Color(180,180,180)));
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        b.setPreferredSize(new Dimension(140, 38));
        return b;
    }
}
