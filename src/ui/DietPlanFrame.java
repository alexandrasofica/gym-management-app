package ui;

import dao.DietPlanDaoJdbcImpl;
import dao.FitnessGoalDaoJdbcImpl;
import model.DietPlan;
import model.FitnessGoal;
import model.User;
import service.DietService;
import service.FitnessService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import service.AiPlanService;

/**
 * Interfața grafică pentru pagina de Plan de dietă
 */
public class DietPlanFrame extends JFrame {

    private final User user;
    private final FitnessService fitnessService;
    private final DietService dietService;

    private FitnessGoal currentGoal;

    private JLabel infoLabel;
    private JTextArea goalDetailsArea;
    private JTextArea planArea;

   
    private final JFrame mainPage;
    /**
     * Constructor
     * inițializează fereastra de plan de dietă
     * @param user
     * @param mainPage
     */
    public DietPlanFrame(User user, JFrame mainPage) {
        this.user = user;
        this.mainPage = mainPage;

        this.fitnessService = new FitnessService(
                new FitnessGoalDaoJdbcImpl(),
                new AiPlanService()
        );

        this.dietService = new DietService(
                new DietPlanDaoJdbcImpl(),
                new AiPlanService()
        );

        init();
        loadGoalFromDb();
        loadLastDietPlanIfExists();
    }

    /**
     * Constructor simplificat
     * @param user
     */
    public DietPlanFrame(User user) {
        this(user, null);
    }
    /**
     * Construiește interfața grafică
     */
    private void init() {
        setTitle("Plan dietă");
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

        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setOpaque(false);

        JLabel title = new JLabel("PLAN DIETĂ");
        title.setForeground(Color.WHITE);
        title.setFont(new Font("Segoe UI", Font.BOLD, 30));
        title.setHorizontalAlignment(SwingConstants.CENTER);

        JButton homeBtn = new JButton("Home");
        styleWhiteButton(homeBtn, 120, 40);
        homeBtn.addActionListener(e -> goHome());

        topBar.add(title, BorderLayout.CENTER);
        topBar.add(homeBtn, BorderLayout.EAST);

        card.add(topBar, BorderLayout.NORTH);


        infoLabel = new JLabel("Obiective fitness: (nu au fost încărcate)");
        infoLabel.setForeground(Color.WHITE);
        infoLabel.setHorizontalAlignment(SwingConstants.CENTER);
        infoLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));

 
        goalDetailsArea = new JTextArea(4, 50);
        goalDetailsArea.setEditable(false);
        goalDetailsArea.setLineWrap(true);
        goalDetailsArea.setWrapStyleWord(true);
        goalDetailsArea.setFont(new Font("Segoe UI", Font.PLAIN, 13));

        JScrollPane goalScroll = new JScrollPane(goalDetailsArea);
        goalScroll.setBorder(BorderFactory.createTitledBorder("Detalii obiective fitness"));
        goalScroll.getViewport().setBackground(Color.WHITE);

  
        planArea = new JTextArea(15, 60);
        planArea.setLineWrap(true);
        planArea.setWrapStyleWord(true);
        planArea.setFont(new Font("Segoe UI", Font.PLAIN, 13));

        JScrollPane planScroll = new JScrollPane(planArea);
        planScroll.setBorder(BorderFactory.createTitledBorder("Plan de dietă"));
        planScroll.getViewport().setBackground(Color.WHITE);

        JPanel center = new JPanel();
        center.setLayout(new BoxLayout(center, BoxLayout.Y_AXIS));
        center.setBackground(Color.WHITE);
        center.setBorder(new EmptyBorder(10, 10, 10, 10));

        JPanel infoWrap = new JPanel(new BorderLayout());
        infoWrap.setOpaque(false);
        infoWrap.add(infoLabel, BorderLayout.CENTER);

        center.add(infoWrap);
        center.add(Box.createVerticalStrut(10));
        center.add(goalScroll);
        center.add(Box.createVerticalStrut(12));
        center.add(planScroll);

        JPanel centerWrapper = new JPanel(new BorderLayout());
        centerWrapper.setBackground(Color.WHITE);
        centerWrapper.setBorder(new EmptyBorder(10, 10, 10, 10));
        centerWrapper.add(center, BorderLayout.CENTER);

        card.add(centerWrapper, BorderLayout.CENTER);

        JButton editGoalBtn = new JButton("Editează obiectivele");
        JButton reloadGoalBtn = new JButton("Reîncarcă obiectivele");
        JButton genBtn = new JButton("Generează plan dietă");
        JButton saveBtn = new JButton("Salvează planul");

        styleWhiteButton(editGoalBtn, 220, 45);
        styleWhiteButton(reloadGoalBtn, 220, 45);
        styleWhiteButton(genBtn, 220, 45);
        styleWhiteButton(saveBtn, 220, 45);

        editGoalBtn.addActionListener(e -> handleEditGoals());
        reloadGoalBtn.addActionListener(e -> loadGoalFromDb());
        genBtn.addActionListener(e -> handleGenerate());
        saveBtn.addActionListener(e -> handleSave());

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 18, 10));
        bottomPanel.setOpaque(false);
        bottomPanel.add(editGoalBtn);
        bottomPanel.add(reloadGoalBtn);
        bottomPanel.add(genBtn);
        bottomPanel.add(saveBtn);

        card.add(bottomPanel, BorderLayout.SOUTH);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.CENTER;
        background.add(card, gbc);

        setContentPane(background);
    }
    /**
     * Aplică un stil standard unui buton
     * @param btn
     * @param w
     * @param h
     */
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

   /**
    * Închide fereastra curentă
    * se întoarce la main page
    */
    private void goHome() {
        dispose();
        if (mainPage != null) {
            mainPage.setVisible(true);
            mainPage.toFront();
        }
    }

   /**
    * Ia ultimul obiectiv al utilizatorului din baza de date
    * dacă nu există, afișează detalii informative în intrfață
    * dacă există, afișează detaliile obiectivului
    */
    private void loadGoalFromDb() {
        currentGoal = fitnessService.getLastGoalForUser(user.getId());

        if (currentGoal == null) {
            infoLabel.setText("Nu există încă obiective fitness pentru acest utilizator.");
            goalDetailsArea.setText(
                    "Nu ai salvat încă obiective.\n" +
                    "Mergi în fereastra \"Obiective fitness\", completează datele și salvează."
            );
        } else {
            infoLabel.setText("Obiective curente: " +
                    currentGoal.getGoalType() +
                    " | activitate: " + currentGoal.getActivityLevel());

            StringBuilder sb = new StringBuilder();
            sb.append("Greutate curentă: ").append(currentGoal.getCurrentWeight()).append(" kg\n");
            sb.append("Greutate țintă:   ").append(currentGoal.getTargetWeight()).append(" kg\n");
            sb.append("Înălțime:         ").append(currentGoal.getHeight()).append(" cm\n");
            sb.append("Obiectiv:         ").append(currentGoal.getGoalType()).append("\n");
            sb.append("Activitate:       ").append(currentGoal.getActivityLevel()).append("\n");
            if (currentGoal.getNotes() != null && !currentGoal.getNotes().isEmpty()) {
                sb.append("Note:            ").append(currentGoal.getNotes()).append("\n");
            }

            goalDetailsArea.setText(sb.toString());
        }
    }
    /**
     * caută ultimul plan de dietă salvat de utilizator
     * verifică dacă acesta există și conține informații valide
     * afișează data salvării
     * afișează conținutul planului
     */
    private void loadLastDietPlanIfExists() {
        DietPlan lastPlan = dietService.getLastPlanForUser(user.getId());
        if (lastPlan != null && lastPlan.getPlanText() != null && !lastPlan.getPlanText().isEmpty()) {
            StringBuilder sb = new StringBuilder();
            sb.append("Plan salvat la: ").append(lastPlan.getCreatedAt()).append("\n\n");
            sb.append(lastPlan.getPlanText());
            planArea.setText(sb.toString());
        }
    }
    /**
     * Deschide fereastra "Obiective fitness"
     * Afișează un Toast Dialog informativ
     */
    private void handleEditGoals() {
        new FitnessGoalsFrame(user).setVisible(true);

        ToastDialog.show(this,
                "După ce salvezi noile obiective în fereastra 'Obiective fitness',\n",
                "apasă butonul 'Reîncarcă obiectivele' și apoi 'Generează plan dietă'.");
    }
    /**
     * verifică dacă utilizatorul are obiective fitness setate
	 * dacă nu există, afișează un ToastDialog de eroare, dacă există:
	 * apelează AI-ul pentru a genera planul de dietă
	 * afișează planul generat în interfață
	 * afișează un ToastDialog de succes
	 * tratează și afișează erorile AI într-un mod vizual personalizat
     */
    private void handleGenerate() {
        if (currentGoal == null) {
            ToastDialog.show(this, "Eroare", 
                    "Nu există obiective fitness pentru acest utilizator.\n" +
                    "Te rog setează mai întâi datele în 'Obiective fitness'.");
            return;
        }

        try {
            String foodPrefs = ""; 
            String dietText = dietService.generateDietPlanAi(user, currentGoal, foodPrefs);
            planArea.setText(dietText);

            ToastDialog.show(this, "Succes", "Planul de dietă a fost generat cu AI.");
        } catch (IllegalStateException ex) {
            ToastDialog.show(this, "Eroare AI", ex.getMessage());
        } catch (RuntimeException ex) {
            ToastDialog.show(this, "Eroare AI", ex.getMessage());
        }
    }


    /**
     * Salvează planul de dietă generat
     * Metoda verifică dacă există un plan de dietă afișat în interfață
     * Dacă zona de text este goală, afișează un mesaj de avertizare.
     * Dacă nu, creează un obiect, îl salvează
     * în baza de date și afișează un mesaj de confirmare utilizatorului
     */
    private void handleSave() {
        String text = planArea.getText().trim();
        if (text.isEmpty()) {
            ToastDialog.show(this,
                    "Nu există niciun plan de salvat.",
            		"Generează mai întâi planul.");
            return;
        }

        double calories = 0;

        DietPlan plan = new DietPlan(user.getId(), calories, text);
        dietService.savePlan(plan);

        ToastDialog.show(this,
                "Plan de dietă salvat." ,
        		"Îl poți vedea în Profil utilizator → Plan dietă.");
    }
}
