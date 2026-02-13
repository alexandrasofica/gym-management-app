package ui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;
import java.util.Vector;

import util.DbConnection;

public class ReservationManagementFrame extends JFrame {
	
    private JTable table;
    private DefaultTableModel tableModel;

    private JButton addBtn;
    private JButton deleteBtn;
    private JButton homeBtn;
    private JButton closeBtn;
    
    
    private final model.User loggedUser;
    
    public ReservationManagementFrame() {
        this(null);
    }

    public ReservationManagementFrame(model.User loggedUser) {
        this.loggedUser = loggedUser;
        init();
        loadData();
    }
    public void reload() {
        loadData();
    }

    private void init() {
        setTitle("Gestionare rezervări");
        setSize(1100, 650);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

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
        JPanel card = new JPanel(new BorderLayout(15, 15));
        card.setPreferredSize(new Dimension(1000, 560));
        card.setBackground(new Color(0, 0, 0, 170));
        card.setBorder(new EmptyBorder(20, 20, 20, 20));
        JLabel title = new JLabel("GESTIONARE REZERVĂRI");
        title.setForeground(Color.WHITE);
        title.setFont(new Font("Segoe UI", Font.BOLD, 30));
        title.setHorizontalAlignment(SwingConstants.CENTER);
        card.add(title, BorderLayout.NORTH);

        tableModel = new DefaultTableModel(
                new Object[]{"ID", "User ID", "Data", "Ora", "Notă"},
                0
        ) {
            @Override
            public boolean isCellEditable(int row, int col) {
                return false; 
            }
        };

        table = new JTable(tableModel);
        table.setRowHeight(28);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getViewport().setBackground(Color.WHITE);

        JPanel tableWrapper = new JPanel(new BorderLayout());
        tableWrapper.setBackground(Color.WHITE);
        tableWrapper.setBorder(new EmptyBorder(10, 10, 10, 10));
        tableWrapper.add(scrollPane, BorderLayout.CENTER);

        card.add(tableWrapper, BorderLayout.CENTER);

        addBtn = new JButton("Adaugă rezervare");
        deleteBtn = new JButton("Șterge rezervare");
        homeBtn = new JButton("Home");
        closeBtn = new JButton("Închide");

        styleWhiteButton(addBtn, 180, 40);
        styleWhiteButton(deleteBtn, 180, 40);
        styleWhiteButton(homeBtn, 120, 40);
        styleWhiteButton(closeBtn, 120, 40);

        addBtn.addActionListener(e -> new ReservationFrame(this, loggedUser).setVisible(true));
        deleteBtn.addActionListener(e -> deleteSelectedReservation());
        homeBtn.addActionListener(e -> dispose());
        closeBtn.addActionListener(e -> dispose());

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 18, 10));
        bottomPanel.setOpaque(false);
        bottomPanel.add(addBtn);
        bottomPanel.add(deleteBtn);
        bottomPanel.add(homeBtn);
        bottomPanel.add(closeBtn);

        card.add(bottomPanel, BorderLayout.SOUTH);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.CENTER;
        background.add(card, gbc);

        setContentPane(background);
    }

    private void styleWhiteButton(JButton btn, int w, int h) {
        btn.setFocusPainted(false);
        btn.setRolloverEnabled(false);     
        btn.setOpaque(true);
        btn.setContentAreaFilled(true);
        btn.setBackground(Color.WHITE);
        btn.setForeground(Color.BLACK);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setBorder(BorderFactory.createLineBorder(new Color(180, 180, 180)));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(w, h));
    }

    private void loadData() {
        tableModel.setRowCount(0);

        try {
            Connection conn = DbConnection.getConnection();
            String sql = "SELECT id, user_id, date, time, note FROM reservations";

            try (PreparedStatement stmt = conn.prepareStatement(sql);
                 ResultSet rs = stmt.executeQuery()) {

                while (rs.next()) {
                    Vector<Object> row = new Vector<>();
                    row.add(rs.getInt("id"));
                    row.add(rs.getInt("user_id"));
                    row.add(rs.getString("date"));
                    row.add(rs.getString("time"));
                    row.add(rs.getString("note"));
                    tableModel.addRow(row);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this,
                    "Eroare la încărcarea rezervărilor: " + e.getMessage());
        }
    }

    private void deleteSelectedReservation() {
        int row = table.getSelectedRow();
        if (row == -1) {
        	ModernDialog.error(this, "Atenție", "Selectează o rezervare din tabel mai întâi.");
            return;
        }

        boolean ok = ModernDialog.confirm(this, "Confirmare",
                "Sigur vrei să ștergi rezervarea selectată?");
        if (!ok) return;


        int reservationId = (int) tableModel.getValueAt(row, 0);

        try {
            Connection conn = DbConnection.getConnection();
            String sql = "DELETE FROM reservations WHERE id = ?";

            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setInt(1, reservationId);
                stmt.executeUpdate();
            }

            ModernDialog.info(this, "Succes", "Rezervare ștearsă.");
            loadData();

        } catch (SQLException e) {
            e.printStackTrace();
            ModernDialog.error(this, "Eroare", "Eroare la stergerea rezervării!\n" + e.getMessage());

        }
    }
}
