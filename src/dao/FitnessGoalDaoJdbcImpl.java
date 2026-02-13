package dao;
/**
 * Clasa implementează interfața 
 * face legătura dintre obiectul FitnessGoal si tabela fitness_goals
 */
import model.FitnessGoal;
import util.DbConnection;

import java.sql.*;
import java.time.LocalDateTime;

public class FitnessGoalDaoJdbcImpl implements FitnessGoalDao {
 
    public FitnessGoalDaoJdbcImpl() {
        
    }
    /**
     * inserează un obiectiv fitness în baza de date
     */
    public void save(FitnessGoal goal) {
        String sql = """
            INSERT INTO fitness_goals
            (user_id, current_weight, target_weight, height,
             goal_type, activity_level, notes, created_at)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?)
            """;
   
        Connection connection = DbConnection.getConnection();

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, goal.getUserId());
            stmt.setDouble(2, goal.getCurrentWeight());
            stmt.setDouble(3, goal.getTargetWeight());
            stmt.setDouble(4, goal.getHeight());
            stmt.setString(5, goal.getGoalType());
            stmt.setString(6, goal.getActivityLevel());
            stmt.setString(7, goal.getNotes());
            stmt.setString(8, goal.getCreatedAt().toString());

            int rows = stmt.executeUpdate();
            System.out.println("FitnessGoal salvat: rows=" + rows +
                    ", user_id=" + goal.getUserId());

        } catch (SQLException e) {
            System.out.println("Eroare la salvarea fitness_goals:");
            e.printStackTrace();
        }
    }

    /**
     * returnează cel mai recent obiectiv fitness pentru un user
     */
    public FitnessGoal findLastByUserId(int userId) {
        String sql = """
            SELECT id, user_id, current_weight, target_weight, height,
                   goal_type, activity_level, notes, created_at
            FROM fitness_goals
            WHERE user_id = ?
            ORDER BY created_at DESC
            LIMIT 1
            """;

        Connection connection = DbConnection.getConnection();

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                FitnessGoal goal = new FitnessGoal(
                        rs.getInt("user_id"),
                        rs.getDouble("current_weight"),
                        rs.getDouble("target_weight"),
                        rs.getDouble("height"),
                        rs.getString("goal_type"),
                        rs.getString("activity_level"),
                        rs.getString("notes")
                );
                goal.setId(rs.getInt("id"));
                try {
                    goal.setCreatedAt(LocalDateTime.parse(rs.getString("created_at")));
                } catch (Exception ignored) {
                   
                }

                return goal;
            }

        } catch (SQLException e) {
            System.out.println("Eroare la citirea fitness_goals:");
            e.printStackTrace();
        }

        return null;
    }
}
