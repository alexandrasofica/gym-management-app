package model;

import java.time.LocalDateTime;
/**
 * Clasa reprezintă un obiectiv fitness al unui utilizator
 */
public class FitnessGoal {

    private int id;
    private int userId;
    private double currentWeight;
    private double targetWeight;
    private double height;
    private String goalType;       
    private String activityLevel; 
    private String notes;       
    private LocalDateTime createdAt;

    public FitnessGoal() {
    }
    /**
     * Constructor
     * @param id
     * @param userId
     * @param currentWeight
     * @param targetWeight
     * @param height
     * @param goalType
     * @param activityLevel
     * @param notes
     * @param createdAt
     */
    public FitnessGoal(int id, int userId, double currentWeight, double targetWeight,
                       double height, String goalType, String activityLevel,
                       String notes, LocalDateTime createdAt) {
        this.id = id;
        this.userId = userId;
        this.currentWeight = currentWeight;
        this.targetWeight = targetWeight;
        this.height = height;
        this.goalType = goalType;
        this.activityLevel = activityLevel;
        this.notes = notes;
        this.createdAt = createdAt;
    }
    /**
     * Constructor secundar
     * Inițializează un obiectiv fitness nou
     * @param userId
     * @param currentWeight
     * @param targetWeight
     * @param height
     * @param goalType
     * @param activityLevel
     * @param notes
     */
    public FitnessGoal(int userId, double currentWeight, double targetWeight,
                       double height, String goalType, String activityLevel,
                       String notes) {
        this(0, userId, currentWeight, targetWeight, height,
             goalType, activityLevel, notes, LocalDateTime.now());
    }


    /**
     * Getter
     * @return id-ul planului fitness
     */
    public int getId() { return id; }
    /**
     * Setter
     * @param id
     * salvează id-ul planului de dietă
     */
    public void setId(int id) { this.id = id; }
    /**
     * Getter
     * @return id-ul utilizatorului
     */
    public int getUserId() { return userId; }
    /**
     * Setter
     * @param userId 
     */
    public void setUserId(int userId) { this.userId = userId; }
    /**
     * Getter
     * @return greutatea curentă
     */
    public double getCurrentWeight() { return currentWeight; }
    /**
     * Setter
     * @param currentWeight
     */
    public void setCurrentWeight(double currentWeight) { this.currentWeight = currentWeight; }
    /**
     * Getter
     * @return greutate țintă
     */
    public double getTargetWeight() { return targetWeight; }
    /**
     * Setter
     * @param targetWeight
     */
    public void setTargetWeight(double targetWeight) { this.targetWeight = targetWeight; }
    /**
     * Getter
     * @return înălțime
     */
    public double getHeight() { return height; }
    /**
     * Setter
     * @param height
     */
    public void setHeight(double height) { this.height = height; }
    /**
     * Getter
     * @return tipul obiectivului
     */
    public String getGoalType() { return goalType; }
    /**
     * Setter
     * @param goalType
     */
    public void setGoalType(String goalType) { this.goalType = goalType; }
    /**
     * Getter
     * @return nivelul de activitate
     */
    public String getActivityLevel() { return activityLevel; }
    /**
     * Setter
     * @param activityLevel
     */
    public void setActivityLevel(String activityLevel) { this.activityLevel = activityLevel; }
    /**
     * Getter
     * @return observații/specificații
     */
    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
    
    /**
     * Getter
     * @return data și ora creării obiectivului
     */
    public LocalDateTime getCreatedAt() { return createdAt; }
    /**
     * Setter
     * modifică manual data creării
     * @param createdAt
     */
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
