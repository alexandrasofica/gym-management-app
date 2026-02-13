package ui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import util.DbConnection;

public class ReservationFrame extends JFrame {

    private JTextField userIdField;
    private JTextField dateField;
    private JTextField timeField;
    private JTextArea noteArea;
    private model.User loggedUser;

    private ReservationManagementFrame parent;

    public ReservationFrame(ReservationManagementFrame parent, model.User loggedUser) {
        this.parent = parent;
        this.loggedUser=loggedUser;
        init();
    }

    public ReservationFrame() {
        init();
    }

    private void init() {
        setTitle("Creare rezervare");
        setSize(900, 550);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        JPanel background = new JPanel() {
            private final Image bg = new ImageIcon(
                    ReservationFrame.class.getResource("/image/login.jpg") // <- schimbă poza dacă vrei
            ).getImage();

            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(bg, 0, 0, getWidth(), getHeight(), this);
            }
        };
        background.setLayout(new GridBagLayout());

        JPanel card = new JPanel(new BorderLayout(0, 18));
        card.setPreferredSize(new Dimension(520, 430));
        card.setBackground(new Color(0, 0, 0, 170));
        card.setBorder(new EmptyBorder(25, 25, 25, 25));

        JLabel title = new JLabel("CREARE REZERVARE");
        title.setHorizontalAlignment(SwingConstants.CENTER);
        title.setForeground(Color.WHITE);
        title.setFont(new Font("Segoe UI", Font.BOLD, 28));
        card.add(title, BorderLayout.NORTH);

        JPanel form = new JPanel(new GridBagLayout());
        form.setOpaque(false);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 8, 10, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        userIdField = new JTextField(16);
        dateField = new JTextField(16);
        timeField = new JTextField(16);
        noteArea = new JTextArea(4, 16);
        noteArea.setLineWrap(true);
        noteArea.setWrapStyleWord(true);

        styleField(userIdField);
        styleField(dateField);
        styleField(timeField);
        styleArea(noteArea);

        int row = 0;

        gbc.gridx = 0; gbc.gridy = row; gbc.weightx = 0;
        form.add(makeLabel("User ID:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1;
        form.add(userIdField, gbc);

        row++;
        gbc.gridx = 0; gbc.gridy = row; gbc.weightx = 0;
        form.add(makeLabel("Data (yyyy-MM-dd):"), gbc);
        gbc.gridx = 1; gbc.weightx = 1;
        form.add(dateField, gbc);

        row++;
        gbc.gridx = 0; gbc.gridy = row; gbc.weightx = 0;
        form.add(makeLabel("Ora (HH:mm):"), gbc);
        gbc.gridx = 1; gbc.weightx = 1;
        form.add(timeField, gbc);

        row++;
        gbc.gridx = 0; gbc.gridy = row; gbc.weightx = 0;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        form.add(makeLabel("Notă / detalii:"), gbc);

        gbc.gridx = 1; gbc.weightx = 1;
        gbc.fill = GridBagConstraints.BOTH;
        JScrollPane noteScroll = new JScrollPane(noteArea);
        noteScroll.setBorder(BorderFactory.createLineBorder(new Color(255, 255, 255, 60), 1));
        noteScroll.getViewport().setBackground(Color.WHITE);
        form.add(noteScroll, gbc);

        card.add(form, BorderLayout.CENTER);

        JButton saveBtn = new JButton("Salvează rezervarea");
        JButton cancelBtn = new JButton("Anulează");

        stylePrimaryButton(saveBtn);
        styleSecondaryButton(cancelBtn);

        saveBtn.addActionListener(e -> handleSave()); // <- NU schimbăm logica
        cancelBtn.addActionListener(e -> dispose());

        JPanel btnPanel = new JPanel(new GridLayout(1, 2, 12, 0));
        btnPanel.setOpaque(false);
        btnPanel.add(saveBtn);
        btnPanel.add(cancelBtn);

        card.add(btnPanel, BorderLayout.SOUTH);

        GridBagConstraints bgc = new GridBagConstraints();
        bgc.gridx = 0;
        bgc.gridy = 0;
        bgc.anchor = GridBagConstraints.CENTER;
        background.add(card, bgc);

        setContentPane(background);
    }
    private JLabel makeLabel(String text) {
        JLabel lbl = new JLabel(text);
        lbl.setForeground(Color.WHITE);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 13));
        return lbl;
    }

    private void styleField(JTextField field) {
        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        field.setPreferredSize(new Dimension(240, 36));
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(255, 255, 255, 60), 1),
                BorderFactory.createEmptyBorder(8, 10, 8, 10)
        ));
    }

    private void styleArea(JTextArea area) {
        area.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        area.setBorder(BorderFactory.createEmptyBorder(8, 10, 8, 10));
    }

    private void stylePrimaryButton(JButton btn) {
        btn.setFocusPainted(false);
        btn.setRolloverEnabled(false);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setBackground(Color.WHITE);
        btn.setForeground(Color.BLACK);
        btn.setBorder(BorderFactory.createLineBorder(new Color(255, 255, 255, 90), 1));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(220, 42));
        btn.setOpaque(true);
    }

    private void styleSecondaryButton(JButton btn) {
        btn.setFocusPainted(false);
        btn.setRolloverEnabled(false);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setBackground(new Color(0, 0, 0, 200));
        btn.setForeground(Color.WHITE);
        btn.setBorder(BorderFactory.createLineBorder(new Color(255, 255, 255, 60), 1));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(220, 42));
        btn.setOpaque(true);
    }

    private void handleSave() {
        String userIdText = userIdField.getText().trim();
        String date = dateField.getText().trim();
        String time = timeField.getText().trim();
        String note = noteArea.getText().trim();

        if (userIdText.isEmpty() || date.isEmpty() || time.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "User ID, data și ora sunt obligatorii!");
            return;
        }

        try {
            int userId = Integer.parseInt(userIdText);

            Connection conn = DbConnection.getConnection();
            String sql = "INSERT INTO reservations (user_id, date, time, note) VALUES (?, ?, ?, ?)";

            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setInt(1, userId);
                stmt.setString(2, date);
                stmt.setString(3, time);
                stmt.setString(4, note);
                stmt.executeUpdate();
            }

            JOptionPane.showMessageDialog(this, "Rezervare salvată cu succes!");
            if (parent != null) {
                parent.reload();
            }
            dispose();

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "User ID trebuie să fie un număr!");
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Eroare la salvarea rezervării: " + ex.getMessage());
        }
    }
}
