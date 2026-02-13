package model;
/**
 * separa planul de dietă de cel de fitness
 * transportă datele intre servicii și UI
 */
public class AiPlanResult {
	/**
	 * textul planului de dietă generat
	 */
    private String dietPlan;
    /**
     * textul planului de fitness generat 
     */
    private String workoutPlan;
    /**
     * Getter
     * @return planul de dietă
     */
	public String getDietPlan() {
		return dietPlan;
	}
	/**
	 * Setter
	 * @param dietPlan
	 * salvează planul de dietă
	 */
	public void setDietPlan(String dietPlan) {
		this.dietPlan = dietPlan;
	}
	/**
	 * Getter
	 * @return planul fitness
	 */
	public String getWorkoutPlan() {
		return workoutPlan;
	}
	/**
	 * Setter
	 * @param workoutPlan
	 * salvează planul fitness
	 */
	public void setWorkoutPlan(String workoutPlan) {
		this.workoutPlan = workoutPlan;
	}
}
