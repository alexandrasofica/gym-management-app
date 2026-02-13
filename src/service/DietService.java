package service;

import dao.DietPlanDao;
import model.DietPlan;
import model.FitnessGoal;
import model.User;

/**
 * Clasa se ocupă exclusiv cu generarea și salvarea
 * planurilor de dietă folosind AI
 */
public class DietService {

    private final DietPlanDao dietPlanDao;
    private final AiPlanService aiPlanService;
    /**
     * Constructor 
     * @param dietPlanDao
     */
    public DietService(DietPlanDao dietPlanDao) {
        this.dietPlanDao = dietPlanDao;
        this.aiPlanService = null;
    }

    /**
     * Constructor AI
     * @param dietPlanDao
     * @param aiPlanService
     */
    public DietService(DietPlanDao dietPlanDao, AiPlanService aiPlanService) {
        this.dietPlanDao = dietPlanDao;
        this.aiPlanService = aiPlanService;
    }

    /**
     * Salvează planul de dietă în baza de date
     * @param plan
     */
    public void savePlan(DietPlan plan) {
        dietPlanDao.save(plan);
    }

    /**
     * Metoda gestionează planul de dietă
     * @param userId
     * @return cel mai recent plan de dietă
     */
    public DietPlan getLastPlanForUser(int userId) {
        return dietPlanDao.findLastByUserId(userId);
    }

    /**
     * Generează plan de dietă folosind AI
     * @param user utilizatorul curent
     * @param goal obiectivul fitness
     * @param foodPreferences preferințe alimentare
     * @return plan de dietă generat de AI
     */
    public String generateDietPlanAi(User user,
            FitnessGoal goal,
            String foodPreferences) {

		if (aiPlanService == null) {
			throw new IllegalStateException("AI Plan Service nu este configurat. Verifică AiPlanService.");
		}
	
		try {
			return aiPlanService.generateAiPlan(
			user,
			goal,
			false,
			foodPreferences,
			""
	);
	} catch (Exception e) {
		throw new RuntimeException("Planul de dietă nu a putut fi generat de AI: " + e.getMessage());
		}
    }
}
