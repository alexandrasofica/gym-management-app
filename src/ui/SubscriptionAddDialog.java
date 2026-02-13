package ui;

import model.SubscriptionType;
import model.UserType;
import service.SubscriptionService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

public class SubscriptionAddDialog extends JDialog {

    private final SubscriptionService subscriptionService;
    private final SubscriptionType type;
    private final UserType userType;
    private final double basePrice;

    private JTextField userIdField;
    private JTextField startDateField;
    private JTextField endDateField;

    public SubscriptionAddDialog(Window owner,
                                 SubscriptionService subscriptionService,
                                 int defaultUserId,
                                 SubscriptionType type,
                                 UserType userType,
                                 double basePrice) {

        super(owner, "Adaugă abonament", ModalityType.APPLICATION_MODAL);
        this.subscriptionService = subscriptionService;
        this.type = type;
        this.userType = userType;
        this.basePrice = basePrice;

        init(defaultUserId);
    }

    private void init(int defaultUserId) {
        setSize(520, 360);
        setLocationRelativeTo(getOwner());
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        JPanel root = new JPanel(new BorderLayout());
        root.setBorder(new EmptyBorder(18, 18, 18, 18));
        root.setBackground(new Color(25, 25, 25));

        JLabel title = new JLabel("Adaugă abonament");
        title.setForeground(Color.WHITE);
        title.setFont(new Font("Segoe UI", Font.BOLD, 26));
        root.add(title, BorderLayout.NORTH);

        JPanel form = new JPanel(new GridBagLayout());
        form.setOpaque(false);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 6, 10, 6);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0; gbc.gridy = 0;

        JLabel info = new JLabel("Tip: " + type + " | Utilizator: " + userType + " | Preț bază: " + basePrice + " lei");
        info.setForeground(new Color(220, 220, 220));
        info.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        gbc.gridwidth = 2;
        form.add(info, gbc);

        gbc.gridwidth = 1;

        gbc.gridy++;
        form.add(makeLabel("User ID:"), gbc);
        gbc.gridx = 1;
        userIdField = makeField();
        userIdField.setText(String.valueOf(defaultUserId));
        form.add(userIdField, gbc);

        gbc.gridx = 0; gbc.gridy++;
        form.add(makeLabel("Data început (yyyy-MM-dd):"), gbc);
        gbc.gridx = 1;
        startDateField = makeField();
        startDateField.setText(LocalDate.now().toString());
        form.add(startDateField, gbc);

        gbc.gridx = 0; gbc.gridy++;
        form.add(makeLabel("Data sfârșit (yyyy-MM-dd):"), gbc);
        gbc.gridx = 1;
        endDateField = makeField();
        endDateField.setText(LocalDate.now().plusMonths(1).toString());
        form.add(endDateField, gbc);

        root.add(form, BorderLayout.CENTER);

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        buttons.setOpaque(false);

        JButton cancel = new JButton("Anulează");
        JButton save = new JButton("Salvează");

        styleGhostButton(cancel);
        styleWhiteButton(save);

        cancel.addActionListener(e -> dispose());
        save.addActionListener(e -> handleSave());

        buttons.add(cancel);
        buttons.add(save);

        root.add(buttons, BorderLayout.SOUTH);

        setContentPane(root);
    }

    private void handleSave() {
        try {
            int userId = Integer.parseInt(userIdField.getText().trim());
            String start = startDateField.getText().trim();
            String end = endDateField.getText().trim();

            if (userId <= 0) {
                JOptionPane.showMessageDialog(this, "User ID trebuie să fie > 0");
                return;
            }
            if (start.isEmpty() || end.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Completează datele (start/end)!");
                return;
            }

            LocalDate.parse(start);
            LocalDate.parse(end);

            subscriptionService.createSubscription(
                    userId,
                    type,
                    userType,
                    basePrice,
                    start,
                    end
            );

            ToastDialog.show(SwingUtilities.getWindowAncestor(this), "Succes", "Abonament salvat!");
            dispose();

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "User ID trebuie să fie număr!");
        } catch (DateTimeParseException ex) {
            JOptionPane.showMessageDialog(this, "Data trebuie format yyyy-MM-dd (ex: 2026-02-12)");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Eroare la salvare: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    private JLabel makeLabel(String text) {
        JLabel l = new JLabel(text);
        l.setForeground(Color.WHITE);
        l.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        return l;
    }

    private JTextField makeField() {
        JTextField f = new JTextField();
        f.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        f.setBackground(Color.WHITE);
        f.setForeground(Color.BLACK);
        f.setBorder(BorderFactory.createEmptyBorder(8, 10, 8, 10));
        return f;
    }

    private void styleWhiteButton(JButton btn) {
        btn.setFocusPainted(false);
        btn.setRolloverEnabled(false);
        btn.setBorderPainted(false);
        btn.setContentAreaFilled(true);
        btn.setOpaque(true);

        btn.setBackground(Color.WHITE);
        btn.setForeground(Color.BLACK);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 16));
        btn.setPreferredSize(new Dimension(140, 45));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    }

    private void styleGhostButton(JButton btn) {
        btn.setFocusPainted(false);
        btn.setRolloverEnabled(false);
        btn.setContentAreaFilled(false);
        btn.setOpaque(false);

        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 16));
        btn.setPreferredSize(new Dimension(140, 45));
        btn.setBorder(BorderFactory.createLineBorder(new Color(255, 255, 255, 80), 1));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    }
}
