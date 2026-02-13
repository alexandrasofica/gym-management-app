package dao;

import model.DietPlan;
import util.DbConnection;

import java.sql.*;
import java.time.LocalDateTime;
/**
 *Clasa gestionează 
 *salvarea
 *citirea 
 *planurilor de dietă din baza de date 
 *folosind JDBC
 */
public class DietPlanDaoJdbcImpl implements DietPlanDao {

	/**
	 * sablon de tip INSERT
	 * locuri libere pt datele de salvat
	 */
    @Override
    public void save(DietPlan plan) {
        String sql = """
            INSERT INTO diet_plans
            (user_id, calories, plan_text, created_at)
            VALUES (?, ?, ?, ?)
            """;

        /**
         * se deschide conexiunea cu baza de date
         */
        Connection connection = DbConnection.getConnection();

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
        	/**
             * inlocuieste "?" cu date reale
             */
            stmt.setInt(1, plan.getUserId());
            stmt.setDouble(2, plan.getCalories());
            stmt.setString(3, plan.getPlanText());
            stmt.setString(4, plan.getCreatedAt().toString());

            /**
             * datele pleaca din java si se scriu in tabel
             */
            int rows = stmt.executeUpdate();
            System.out.println("DietPlan salvat: rows=" + rows +
                    ", user_id=" + plan.getUserId());
        } catch (SQLException e) {
            System.out.println("Eroare la salvarea în diet_plans:");
            e.printStackTrace();
        }
    }
    /**
     * returneaza ultima dietă dupa ID User 
     */
    @Override
    public DietPlan findLastByUserId(int userId) {
        String sql = """
            SELECT id, user_id, calories, plan_text, created_at
            FROM diet_plans
            WHERE user_id = ?
            ORDER BY created_at DESC
            LIMIT 1
            """;

        Connection connection = DbConnection.getConnection();

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();

            /**
             * daca tabel gol pentru acel user
             * se creeaza obiect gol incare descarcam datele din tabel
             */
            if (rs.next()) {
                DietPlan plan = new DietPlan();
                plan.setId(rs.getInt("id"));
                plan.setUserId(rs.getInt("user_id"));	
                plan.setCalories(rs.getDouble("calories"));
                plan.setPlanText(rs.getString("plan_text"));

                String createdAtStr = rs.getString("created_at");
                try {
                    plan.setCreatedAt(LocalDateTime.parse(createdAtStr));
                } catch (Exception ignored) {
                }

                return plan;
            }
            /**
             * eroare legata de baza de date
             * variabila e contine detaliile erorii
             * se afiseaza un mesaj personalizat
             * ajuta la identificarea zonei din cod unde avem eroare
             * afiseaza in consola traseul erorii
             */
        } catch (SQLException e) {
            System.out.println("Eroare la citirea din diet_plans:");
            e.printStackTrace();
        }

        return null;
    }
}
