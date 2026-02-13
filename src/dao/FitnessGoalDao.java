package dao;
/**
 * Clasa stabilește ce poate face orice clasa DAO fitness
 * se separă logica de implementarea bazei de date
 * schimbarea ușoară a modului de stocare
 */
import model.FitnessGoal;

public interface FitnessGoalDao {
	/**
	 * Salvează obiectivul fitness pentru user 
	 * @param goal
	 */

    void save(FitnessGoal goal);
    /**
     * Caută cel mai recent obiectiv fitness după user
     * @param userId
     * @return obiectivul fitness
     */
    FitnessGoal findLastByUserId(int userId);
}
