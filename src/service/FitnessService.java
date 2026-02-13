package service;

import dao.FitnessGoalDao;
import model.FitnessGoal;
import model.User;

/**
 * Service pentru fitness, generează plan AI
 */
public class FitnessService {

    private final FitnessGoalDao fitnessGoalDao;
    private final AiPlanService aiPlanService;

    /**
     * Constructor AI
     */
    public FitnessService(FitnessGoalDao fitnessGoalDao, AiPlanService aiPlanService) {
        this.fitnessGoalDao = fitnessGoalDao;
        this.aiPlanService = aiPlanService;
    }

    /**
     * Salvează obiectivul fitness în baza de date
     * @param goal
     */
    public void saveGoal(FitnessGoal goal) {
        fitnessGoalDao.save(goal);
    }

    /**
     * Getter
     * @return cel mai recent obiectiv setat de un user
     */
    public FitnessGoal getLastGoalForUser(int userId) {
        return fitnessGoalDao.findLastByUserId(userId);
    }

    /**
     * Calculează BMI = kg / (m^2)
     */
    public double calcBMI(double weight, double heightCm) {
        double h = heightCm / 100.0;
        return weight / (h * h);
    }

    /**
     * Generează planul (dietă + antrenament + obiective)
     * @return textul generat de AI
     */
    public String generateWorkoutPlanAi(User user,
                                        FitnessGoal goal,
                                        boolean abdomen,
                                        String foodPreferences) {

        if (aiPlanService == null) {
            throw new IllegalStateException("AI Plan Service nu este configurat.");
        }

        try {
            return aiPlanService.generateAiPlan(
                    user,
                    goal,
                    abdomen,
                    foodPreferences,
                    "" 
            );
        } catch (Exception e) {
            throw new RuntimeException("Planul nu a putut fi generat de AI: " + e.getMessage());
        }
    }
}
