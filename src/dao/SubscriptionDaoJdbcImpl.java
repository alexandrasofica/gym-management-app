package dao;
/**
 * Implementează clasa SubscriptionDao
 * face legăura dintre obiectul Subscription și tabela subscriptions
 * permite salvarea, afișare, ștergerea și filtrarea abonamentelor
 */
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import model.Subscription;
import model.SubscriptionType;
import model.UserType;
import util.DbConnection;

public class SubscriptionDaoJdbcImpl implements SubscriptionDao {
	/**
	 * IMPLEMENTAREA METODELOR DIN SubscriptionDao
	 */
    private Connection connection;

    /**
     * Constructor
     */
    public SubscriptionDaoJdbcImpl() {
        this.connection = DbConnection.getConnection();
    }
    /**
     * Sablon pentru datele de introdus
     */
    @Override
    public void save(Subscription s) {
        String sql = """
            INSERT INTO subscriptions
            (user_id, type, user_type, base_price, final_price, start_date, end_date, status)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?)
            """;
        /**
         * se inlocuieste cu date reale
         */
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, s.getUserId());
            stmt.setString(2, s.getType().name());
            stmt.setString(3, s.getUserType().name());
            stmt.setDouble(4, s.getBasePrice());
            stmt.setDouble(5, s.getFinalPrice());
            stmt.setString(6, s.getStartDate().toString());
            stmt.setString(7, s.getEndDate().toString());
            stmt.setString(8, s.getStatus());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Metoda de vizualizare toate abonamentele
     */
    @Override
    public List<Subscription> findAll() {
        String sql = "SELECT * FROM subscriptions";
        List<Subscription> list = new ArrayList<>();

        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                list.add(mapRow(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

    /**
     * Filtrare dupa tipul abonamentului
     */
    @Override
    public List<Subscription> findByType(SubscriptionType type) {
        String sql = "SELECT * FROM subscriptions WHERE type = ?";
        List<Subscription> list = new ArrayList<>();

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, type.name());
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    list.add(mapRow(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }
    /**
     * Filtrare dupa tipul user-ului
     */

    @Override
    public List<Subscription> findByUserType(UserType userType) {
        String sql = "SELECT * FROM subscriptions WHERE user_type = ?";
        List<Subscription> list = new ArrayList<>();

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, userType.name());
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    list.add(mapRow(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }
    
    /**
     * Statusul abonamentului ACTIV/EXPIRAT
     */
    @Override
    public List<Subscription> findByStatus(String status) {
        String sql = "SELECT * FROM subscriptions WHERE status = ?";
        List<Subscription> list = new ArrayList<>();

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, status);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    list.add(mapRow(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }
    /**
     * Metoda de mapare
     * transforma datele din tabel in obiecte Java
     * se creeaza un obiect nou
     * se pun in el datele din tabel
     * @param rs
     * @return
     * @throws SQLException
     */
    private Subscription mapRow(ResultSet rs) throws SQLException {
        Subscription s = new Subscription();

        s.setId(rs.getInt("id"));
        s.setUserId(rs.getInt("user_id"));
        s.setType(SubscriptionType.valueOf(rs.getString("type")));
        s.setUserType(UserType.valueOf(rs.getString("user_type")));
        s.setBasePrice(rs.getDouble("base_price"));
        s.setFinalPrice(rs.getDouble("final_price"));
        s.setStartDate(LocalDate.parse(rs.getString("start_date")));
        s.setEndDate(LocalDate.parse(rs.getString("end_date")));
        s.setStatus(rs.getString("status"));

        return s;
    }
    
    /**
     * Metoda de stergere
     */
    @Override
    public void deleteByUserId(int userId) {
        String sql = "DELETE FROM subscriptions WHERE user_id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
