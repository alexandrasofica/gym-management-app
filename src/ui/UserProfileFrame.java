package ui;

import dao.UserDao;
import dao.FitnessGoalDaoJdbcImpl;
import dao.DietPlanDaoJdbcImpl;
import model.User;
import model.FitnessGoal;
import model.DietPlan;
import service.FitnessService;
import service.DietService;
import util.DbConnection;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;
import service.AiPlanService;
public class UserProfileFrame extends JFrame {

    private final User user;
    private final UserDao userDao;

    private final FitnessService fitnessService;
    private final DietService dietService;

    private JTextField idField;
    private JTextField nameField;
    private JTextField emailField;

    private JTable subscriptionsTable;
    private DefaultTableModel subscriptionsModel;

    private JTable reservationsTable;
    private DefaultTableModel reservationsModel;

    private JTextArea fitnessPlanArea;
    private JTextArea dietPlanArea;

    public UserProfileFrame(User user, UserDao userDao) {
        this.user = user;
        this.userDao = userDao;

        this.fitnessService = new FitnessService(
                new FitnessGoalDaoJdbcImpl(),
                new AiPlanService()
        );

        this.dietService = new DietService(
                new DietPlanDaoJdbcImpl(),
                new AiPlanService()
        );

        initFrame();
        loadUserData();
        loadUserSubscriptions();
        loadUserReservations();
        loadFitnessPlan();
        loadDietPlan();
    }
    

    private void initFrame() {
        setTitle("Profil utilizator");
        setSize(1100, 650);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        JPanel background = new JPanel() {
            private final Image bg = new ImageIcon(
                    UserProfileFrame.class.getResource("/image/login.jpg")
            ).getImage();

            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(bg, 0, 0, getWidth(), getHeight(), this);
            }
        };
        background.setLayout(new GridBagLayout());

        JPanel card = new JPanel(new BorderLayout(12, 12));
        card.setPreferredSize(new Dimension(1000, 560));
        card.setBackground(new Color(0, 0, 0, 170));
        card.setBorder(new EmptyBorder(18, 18, 18, 18));

        String email = (user != null && user.getEmail() != null) ? user.getEmail() : "";
        JLabel titleLabel = new JLabel("Profil utilizator - " + email);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 26));

        JButton homeBtn = new JButton("Home");
        styleWhiteButton(homeBtn, 110, 38);
        homeBtn.addActionListener(e -> dispose());

        JPanel top = new JPanel(new BorderLayout());
        top.setOpaque(false);
        top.add(titleLabel, BorderLayout.CENTER);

        JPanel rightTop = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        rightTop.setOpaque(false);
        rightTop.add(homeBtn);
        top.add(rightTop, BorderLayout.EAST);

        card.add(top, BorderLayout.NORTH);

        JTabbedPane tabs = new JTabbedPane();
        tabs.setFont(new Font("Segoe UI", Font.BOLD, 13));

        tabs.addTab("Profil", buildProfilePanel());
        tabs.addTab("Abonamente", buildSubscriptionsPanel());
        tabs.addTab("Rezervări", buildReservationsPanel());
        tabs.addTab("Plan fitness", buildFitnessPanel());
        tabs.addTab("Plan dietă", buildDietPanel());

        card.add(tabs, BorderLayout.CENTER);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.CENTER;
        background.add(card, gbc);

        setContentPane(background);
    }

    private JPanel buildGym() {
        JPanel wrap = new JPanel(new GridBagLayout());
        wrap.setOpaque(false);

        JPanel formCard = new JPanel();
        formCard.setBackground(Color.WHITE);
        formCard.setBorder(new EmptyBorder(25, 25, 25, 25));
        formCard.setPreferredSize(new Dimension(520, 240));
        formCard.setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;
	
        wrap.add(formCard);
        return wrap;
    }

	private JPanel buildProfilePanel() {
        JPanel wrap = new JPanel(new GridBagLayout());
        wrap.setOpaque(false);

        JPanel formCard = new JPanel();
        formCard.setBackground(Color.WHITE);
        formCard.setBorder(new EmptyBorder(25, 25, 25, 25));
        formCard.setPreferredSize(new Dimension(520, 240));
        formCard.setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        int row = 0;

        gbc.gridx = 0; gbc.gridy = row;
        formCard.add(new JLabel("User ID:"), gbc);

        idField = new JTextField(20);
        idField.setEditable(false);
        gbc.gridx = 1;
        formCard.add(idField, gbc);

        row++;
        gbc.gridx = 0; gbc.gridy = row;
        formCard.add(new JLabel("Nume:"), gbc);

        nameField = new JTextField(20);
        gbc.gridx = 1;
        formCard.add(nameField, gbc);

        row++;
        gbc.gridx = 0; gbc.gridy = row;
        formCard.add(new JLabel("Email:"), gbc);

        emailField = new JTextField(20);
        emailField.setEditable(false);
        gbc.gridx = 1;
        formCard.add(emailField, gbc);

        row++;
        JButton saveBtn = new JButton("Salvează profil");
        styleBlackButton(saveBtn, 180, 38);
        saveBtn.addActionListener(e -> handleSaveProfile());

        gbc.gridx = 1; gbc.gridy = row;
        gbc.anchor = GridBagConstraints.EAST;
        formCard.add(saveBtn, gbc);

        wrap.add(formCard);
        return wrap;
    }

    private void loadUserData() {
        if (user == null) return;
        idField.setText(String.valueOf(user.getId()));    
        nameField.setText(user.getName());
        emailField.setText(user.getEmail());
    }

    private void handleSaveProfile() {
        user.setName(nameField.getText().trim());
        userDao.update(user);
        JOptionPane.showMessageDialog(this, "Profil actualizat.");
    }

    private JPanel buildSubscriptionsPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);

        subscriptionsModel = new DefaultTableModel(
                new Object[]{"ID", "User ID", "Tip", "Tip utilizator", "Preț bază", "Preț final", "Start", "End", "Status"},
                0
        ) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };

        subscriptionsTable = new JTable(subscriptionsModel);
        styleTable(subscriptionsTable);

        JScrollPane sp = new JScrollPane(subscriptionsTable);
        sp.setBorder(BorderFactory.createEmptyBorder());
        sp.getViewport().setBackground(Color.WHITE);

        JPanel whiteBox = new JPanel(new BorderLayout());
        whiteBox.setBackground(Color.WHITE);
        whiteBox.setBorder(new EmptyBorder(10, 10, 10, 10));
        whiteBox.add(sp, BorderLayout.CENTER);

        panel.add(whiteBox, BorderLayout.CENTER);
        return panel;
    }

    private void loadUserSubscriptions() {
        if (subscriptionsModel == null || user == null) return;

        subscriptionsModel.setRowCount(0);

        String sql = """
                SELECT id, user_id, type, user_type, base_price, final_price, start_date, end_date, status
                FROM subscriptions
                WHERE user_id = ?
                ORDER BY id DESC
                """;

        try (Connection conn = DbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, user.getId());

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Vector<Object> row = new Vector<>();
                    row.add(rs.getInt("id"));
                    row.add(rs.getInt("user_id"));
                    row.add(rs.getString("type"));
                    row.add(rs.getString("user_type"));
                    row.add(rs.getDouble("base_price"));
                    row.add(rs.getDouble("final_price"));
                    row.add(rs.getString("start_date"));
                    row.add(rs.getString("end_date"));
                    row.add(rs.getString("status"));
                    subscriptionsModel.addRow(row);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this,
                    "Eroare la încărcarea abonamentelor: " + e.getMessage());
        }
    }

    private JPanel buildReservationsPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);

        reservationsModel = new DefaultTableModel(
                new Object[]{"ID", "User ID", "Data", "Ora", "Notă"},
                0
        ) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };

        reservationsTable = new JTable(reservationsModel);
        styleTable(reservationsTable);

        JScrollPane sp = new JScrollPane(reservationsTable);
        sp.setBorder(BorderFactory.createEmptyBorder());
        sp.getViewport().setBackground(Color.WHITE);

        JPanel whiteBox = new JPanel(new BorderLayout());
        whiteBox.setBackground(Color.WHITE);
        whiteBox.setBorder(new EmptyBorder(10, 10, 10, 10));
        whiteBox.add(sp, BorderLayout.CENTER);

        panel.add(whiteBox, BorderLayout.CENTER);
        return panel;
    }

    private void loadUserReservations() {
        if (reservationsModel == null || user == null) return;

        reservationsModel.setRowCount(0);

        String sql = """
                SELECT id, user_id, date, time, note
                FROM reservations
                WHERE user_id = ?
                ORDER BY id DESC
                """;

        try (Connection conn = DbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, user.getId());

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Vector<Object> row = new Vector<>();
                    row.add(rs.getInt("id"));
                    row.add(rs.getInt("user_id"));
                    row.add(rs.getString("date"));
                    row.add(rs.getString("time"));
                    row.add(rs.getString("note"));
                    reservationsModel.addRow(row);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this,
                    "Eroare la încărcarea rezervărilor: " + e.getMessage());
        }
    }

    private JPanel buildFitnessPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);

        fitnessPlanArea = new JTextArea();
        fitnessPlanArea.setEditable(false);
        fitnessPlanArea.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        fitnessPlanArea.setLineWrap(true);
        fitnessPlanArea.setWrapStyleWord(true);

        JScrollPane sp = new JScrollPane(fitnessPlanArea);
        sp.setBorder(BorderFactory.createEmptyBorder());

        JPanel whiteBox = new JPanel(new BorderLayout());
        whiteBox.setBackground(Color.WHITE);
        whiteBox.setBorder(new EmptyBorder(10, 10, 10, 10));
        whiteBox.add(sp, BorderLayout.CENTER);

        panel.add(whiteBox, BorderLayout.CENTER);
        return panel;
    }

    private JPanel buildDietPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);

        dietPlanArea = new JTextArea();
        dietPlanArea.setEditable(false);
        dietPlanArea.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        dietPlanArea.setLineWrap(true);
        dietPlanArea.setWrapStyleWord(true);

        JScrollPane sp = new JScrollPane(dietPlanArea);
        sp.setBorder(BorderFactory.createEmptyBorder());

        JPanel whiteBox = new JPanel(new BorderLayout());
        whiteBox.setBackground(Color.WHITE);
        whiteBox.setBorder(new EmptyBorder(10, 10, 10, 10));
        whiteBox.add(sp, BorderLayout.CENTER);

        panel.add(whiteBox, BorderLayout.CENTER);
        return panel;
    }

    private void loadFitnessPlan() {
        FitnessGoal last = fitnessService.getLastGoalForUser(user.getId());
        if (last == null) {
            fitnessPlanArea.setText(
                    "Nu există încă obiective fitness salvate pentru acest utilizator.\n" +
                    "Mergi la 'Obiective fitness' din meniul principal pentru a le seta."
            );
            return;
        }

        if (last.getNotes() != null && !last.getNotes().trim().isEmpty()) {
            fitnessPlanArea.setText(last.getNotes());
            return;
        }

        String saved = last.getNotes();
        if (saved != null && !saved.trim().isEmpty()) {
            fitnessPlanArea.setText(saved);
        } else {
            fitnessPlanArea.setText("Nu există încă un plan fitness AI salvat pentru acest utilizator.");
        }

    }
 
    private void loadDietPlan() {
        DietPlan last = dietService.getLastPlanForUser(user.getId());
        if (last != null && last.getPlanText() != null && !last.getPlanText().isEmpty()) {
            StringBuilder sb = new StringBuilder();
            if (last.getCreatedAt() != null) {
                sb.append("Plan salvat la: ").append(last.getCreatedAt()).append("\n\n");
            }
            sb.append(last.getPlanText());
            dietPlanArea.setText(sb.toString());
        } else {
            dietPlanArea.setText("Nu există încă un plan de dietă salvat pentru acest utilizator.");
        }
    }

    private void styleTable(JTable t) {
        t.setRowHeight(28);
        t.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        t.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        t.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
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

    private void styleBlackButton(JButton btn, int w, int h) {
        btn.setFocusPainted(false);
        btn.setRolloverEnabled(false);
        btn.setOpaque(true);
        btn.setContentAreaFilled(true);
        btn.setBackground(new Color(35, 35, 35));
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setBorder(BorderFactory.createEmptyBorder(8, 14, 8, 14));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(w, h));
    }
}
