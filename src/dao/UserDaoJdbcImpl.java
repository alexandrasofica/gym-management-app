package dao;
/**
 * Implementarea UserDao
 * legătura între obiectul User și tabela users
 * permite salvarea, căutarea și actualizarea utilizatorilor
 * crearea automată a unui admin
 */
import model.User;
import util.DbConnection;

import java.sql.*;

public class UserDaoJdbcImpl implements UserDao {

    public UserDaoJdbcImpl() {
    }

    /**
     * Ia conexiunea actuala
     * @return
     */
    private Connection getConnection() {
        return DbConnection.getConnection();
    }

    /**
     * Metoda de mapare
     * @param rs
     * @return cheia de acces catre baza de date
     * @throws SQLException
     */
    private User mapUser(ResultSet rs) throws SQLException {
        User u = new User();
        u.setId(rs.getInt("id"));
        u.setName(rs.getString("name"));
        u.setEmail(rs.getString("email"));
        u.setPassword(rs.getString("password"));
        u.setRole(rs.getString("role"));
        return u;
    }

    @Override
    public User findByEmail(String email) {
        String sql = "SELECT * FROM users WHERE email = ?";

        Connection conn = getConnection();
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return mapUser(rs);
            }
        } catch (SQLException e) {
            System.out.println("Eroare la findByEmail:");
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public User findById(int id) {
        String sql = "SELECT * FROM users WHERE id = ?";

        Connection conn = getConnection();
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return mapUser(rs);
            }
        } catch (SQLException e) {
            System.out.println("Eroare la findById:");
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void save(User user) {
        String sql = "INSERT INTO users(name, email, password, role) VALUES (?, ?, ?, ?)";

        Connection conn = getConnection();
        try (PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, user.getName());
            stmt.setString(2, user.getEmail());
            stmt.setString(3, user.getPassword());
            stmt.setString(4, user.getRole());

            int rows = stmt.executeUpdate();
            if (rows > 0) {
                ResultSet keys = stmt.getGeneratedKeys();
                if (keys.next()) {
                    user.setId(keys.getInt(1));
                }
            }
        } catch (SQLException e) {
            System.out.println("Eroare la save user:");
            e.printStackTrace();
        }
    }

    @Override
    public void update(User user) {
        String sql = """
            UPDATE users
            SET name = ?, email = ?, password = ?, role = ?
            WHERE id = ?
            """;

        Connection conn = getConnection();
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, user.getName());
            stmt.setString(2, user.getEmail());
            stmt.setString(3, user.getPassword());
            stmt.setString(4, user.getRole());
            stmt.setInt(5, user.getId());

            int rows = stmt.executeUpdate();
            System.out.println("User updatat: rows=" + rows + ", id=" + user.getId());
        } catch (SQLException e) {
            System.out.println("Eroare la update user:");
            e.printStackTrace();
        }
    }
    /**
     * verifica daca exista un admin
     * daca nu exista inseram noi unul
     */
    @Override
    public void createDefaultAdminIfMissing() {
        
        String checkSql = "SELECT COUNT(*) FROM users WHERE role = 'ADMIN'";

        Connection conn = getConnection();
        try (PreparedStatement stmt = conn.prepareStatement(checkSql)) {
            ResultSet rs = stmt.executeQuery();
            if (rs.next() && rs.getInt(1) > 0) {
                // deja există admin, nu mai facem nimic
                return;
            }
        } catch (SQLException e) {
            System.out.println("Eroare la verificarea adminului:");
            e.printStackTrace();
        }

        /**
         * daca nu exista admin inseram unul
         */
        User admin = new User();
        admin.setName("Admin");
        admin.setEmail("admin@email.com");
        admin.setPassword("parola123");
        admin.setRole("ADMIN");

        save(admin);
        System.out.println("Admin creat implicit cu email=admin@email.com / parola=parola123");
    }
}
