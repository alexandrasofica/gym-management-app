package model;
/**
 * logica aplicației
 * salvare/citire din BD
 * afișare plan de dietă
 */
import java.time.LocalDateTime;

public class DietPlan {

    private int id;
    private int userId;
    private double calories;
    private String planText;
    private LocalDateTime createdAt;
    /**
     * Constructor
     * @param userId
     * @param calories
     * @param planText
     */
    public DietPlan(int userId, double calories, String planText) {
        this.userId = userId;
        this.calories = calories;
        this.planText = planText;
        this.createdAt = LocalDateTime.now();
    }
    /**
     * Constructor fără parametrii
     */
    public DietPlan() {
    }
    
    /**
     * Getter
     * @return id-ul planului
     */
    public int getId() {
        return id;
    }
    /**
     * Setter
     * @param id 
     * salvează id-ul planului
     */
    public void setId(int id) {
        this.id = id;
    }
    /**
     * Getter
     * @return id-ul utilizatorului
     */
    public int getUserId() {
        return userId;
    }
    /**
     * Setter
     * @param userId
     * salvează id-ul utilizatorului
     */
    public void setUserId(int userId) {
        this.userId = userId;
    }
    /**
     * Getter
     * @return caloriile salvate în "Obiective fitness"
     */
    public double getCalories() {
        return calories;
    }
    
    /**
     * Setter
     * @param calories
     * Salvează caloriile
     */
    public void setCalories(double calories) {
        this.calories = calories;
    }

    /**
     * Getter
     * @return planul de dietă
     */
    public String getPlanText() {
        return planText;
    }
    /**
     * Setter
     * @param planText
     */
    public void setPlanText(String planText) {
        this.planText = planText;
    }
    /**
     * Getter
     * @return
     */
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    /**
     * Setter
     * @param createdAt
     */
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
