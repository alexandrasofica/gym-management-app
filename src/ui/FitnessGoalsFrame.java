package ui;

import dao.FitnessGoalDaoJdbcImpl;
import model.FitnessGoal;
import model.User;
import service.FitnessService;
import service.AiPlanService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class FitnessGoalsFrame extends JFrame {

    private final User user;
    private final FitnessService fitnessService;

    private JTextField userIdField;
    private JTextField currentWeightField;
    private JTextField targetWeightField;
    private JTextField heightField;
    private JComboBox<String> goalTypeBox;
    private JComboBox<String> activityBox;
    private JCheckBox abdomenCheck;
    private JTextArea resultArea;

    public FitnessGoalsFrame(User user) {
        this.user = user;

        this.fitnessService = new FitnessService(
                new FitnessGoalDaoJdbcImpl(),
                new AiPlanService()
        );

        init();
        loadLastGoalIfExists();
    }

    private void init() {
        setTitle("Obiective fitness & plan exerciții");
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

        JLabel title = new JLabel("OBIECTIVE FITNESS & PLAN EXERCIȚII");
        title.setForeground(Color.WHITE);
        title.setFont(new Font("Segoe UI", Font.BOLD, 28));
        title.setHorizontalAlignment(SwingConstants.CENTER);
        card.add(title, BorderLayout.NORTH);

        JPanel formWrap = new JPanel(new GridBagLayout());
        formWrap.setOpaque(false);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        userIdField = new JTextField(10);
        userIdField.setText(String.valueOf(user.getId()));
        userIdField.setEditable(false);

        currentWeightField = new JTextField(12);
        targetWeightField = new JTextField(12);
        heightField = new JTextField(12);

        goalTypeBox = new JComboBox<>(new String[]{"SLABIRE", "MASA", "SLABIRE+MASA"});
        activityBox = new JComboBox<>(new String[]{"SEDENTAR", "MODERAT", "ACTIV"});

        abdomenCheck = new JCheckBox("Vreau să lucrez și abdomen");
        abdomenCheck.setOpaque(false);
        abdomenCheck.setForeground(Color.WHITE);
        abdomenCheck.setFont(new Font("Segoe UI", Font.PLAIN, 13));

        styleField(userIdField);
        styleField(currentWeightField);
        styleField(targetWeightField);
        styleField(heightField);
        styleCombo(goalTypeBox);
        styleCombo(activityBox);

        int row = 0;

        addLabel(formWrap, gbc, row, "User ID:");
        addComp(formWrap, gbc, row++, userIdField);

        addLabel(formWrap, gbc, row, "Greutate curentă (kg):");
        addComp(formWrap, gbc, row++, currentWeightField);

        addLabel(formWrap, gbc, row, "Greutate țintă (kg):");
        addComp(formWrap, gbc, row++, targetWeightField);

        addLabel(formWrap, gbc, row, "Înălțime (cm):");
        addComp(formWrap, gbc, row++, heightField);

        addLabel(formWrap, gbc, row, "Tip obiectiv:");
        addComp(formWrap, gbc, row++, goalTypeBox);

        addLabel(formWrap, gbc, row, "Nivel activitate:");
        addComp(formWrap, gbc, row++, activityBox);

        gbc.gridx = 1;
        gbc.gridy = row++;
        formWrap.add(abdomenCheck, gbc);

        resultArea = new JTextArea(12, 40);
        resultArea.setEditable(false);
        resultArea.setLineWrap(true);
        resultArea.setWrapStyleWord(true);
        resultArea.setFont(new Font("Segoe UI", Font.PLAIN, 13));

        JScrollPane scroll = new JScrollPane(resultArea);
        scroll.getViewport().setBackground(Color.WHITE);

        JPanel center = new JPanel(new BorderLayout(12, 12));
        center.setOpaque(false);

        JPanel left = new JPanel(new BorderLayout());
        left.setOpaque(false);
        left.add(formWrap, BorderLayout.NORTH);

        JPanel right = new JPanel(new BorderLayout());
        right.setOpaque(false);
        right.add(new JLabel("Plan generat"), BorderLayout.NORTH);
        right.add(scroll, BorderLayout.CENTER);

        center.add(left, BorderLayout.WEST);
        center.add(right, BorderLayout.CENTER);

        card.add(center, BorderLayout.CENTER);

        JButton genBtn = new JButton("Generează plan AI");
        JButton saveBtn = new JButton("Salvează planul");
        JButton homeBtn = new JButton("Home");

        genBtn.addActionListener(e -> handleGenerate());
        saveBtn.addActionListener(e -> handleSave());
        homeBtn.addActionListener(e -> dispose());

        JPanel bottom = new JPanel(new FlowLayout());
        bottom.setOpaque(false);
        bottom.add(genBtn);
        bottom.add(saveBtn);
        bottom.add(homeBtn);

        card.add(bottom, BorderLayout.SOUTH);

        background.add(card);
        setContentPane(background);
    }

    private void handleGenerate() {
        try {
            double cw = Double.parseDouble(currentWeightField.getText().trim());
            double tw = Double.parseDouble(targetWeightField.getText().trim());
            double h  = Double.parseDouble(heightField.getText().trim());

            String goalType = (String) goalTypeBox.getSelectedItem();
            String activity = (String) activityBox.getSelectedItem();
            boolean abdomen = abdomenCheck.isSelected();

            FitnessGoal goal = new FitnessGoal(
                    user.getId(), cw, tw, h, goalType, activity, ""
            );

            String plan = fitnessService.generateWorkoutPlanAi(
                    user,
                    goal,
                    abdomen,
                    ""  
            );
            resultArea.setText(plan);


        } catch (NumberFormatException ex) {
            ModernDialog.error(this, "Eroare", "Verifică valorile numerice.");
        }
    }

    private void handleSave() {
        if (resultArea.getText().trim().isEmpty()) {
            ModernDialog.error(this, "Eroare", "Generează mai întâi un plan.");
            return;
        }

        FitnessGoal goal = new FitnessGoal(
                user.getId(),
                Double.parseDouble(currentWeightField.getText().trim()),
                Double.parseDouble(targetWeightField.getText().trim()),
                Double.parseDouble(heightField.getText().trim()),
                (String) goalTypeBox.getSelectedItem(),
                (String) activityBox.getSelectedItem(),
                resultArea.getText()
        );

        fitnessService.saveGoal(goal);
        ModernDialog.info(this, "Succes", "Plan salvat cu succes.");
    }

    private void addLabel(JPanel p, GridBagConstraints gbc, int row, String text) {
        JLabel lbl = new JLabel(text);
        lbl.setForeground(Color.WHITE);
        gbc.gridx = 0;
        gbc.gridy = row;
        p.add(lbl, gbc);
    }

    private void addComp(JPanel p, GridBagConstraints gbc, int row, JComponent c) {
        gbc.gridx = 1;
        gbc.gridy = row;
        p.add(c, gbc);
    }

    private void styleField(JTextField f) {
        f.setPreferredSize(new Dimension(220, 32));
    }

    private void styleCombo(JComboBox<?> combo) {
        combo.setPreferredSize(new Dimension(220, 32));
    }

    private void loadLastGoalIfExists() {
        var last = fitnessService.getLastGoalForUser(user.getId());
        if (last != null) {
            currentWeightField.setText(String.valueOf(last.getCurrentWeight()));
            targetWeightField.setText(String.valueOf(last.getTargetWeight()));
            heightField.setText(String.valueOf(last.getHeight()));
            goalTypeBox.setSelectedItem(last.getGoalType());
            activityBox.setSelectedItem(last.getActivityLevel());
            resultArea.setText(last.getNotes());
        }
    }
}
