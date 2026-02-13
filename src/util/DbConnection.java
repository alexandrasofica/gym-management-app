package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DbConnection {

    private static final String URL = "jdbc:sqlite:gym.db";
    private static Connection connection;

    public static Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                Class.forName("org.sqlite.JDBC");
                connection = DriverManager.getConnection(URL);
                System.out.println("Conexiune DB realizată.");
                try (Statement s = connection.createStatement()) {
                    s.execute("PRAGMA foreign_keys = ON;");
                }
                initSchema();
            }

        } catch (ClassNotFoundException e) {
            System.out.println("EROARE: Driverul SQLite nu a fost găsit!");
            e.printStackTrace();
        } catch (SQLException e) {
            System.out.println("EROARE SQL în DbConnection:");
            e.printStackTrace();
        }

        return connection;
    }

    private static void initSchema() throws SQLException {
        try (Statement stmt = connection.createStatement()) {
            stmt.execute("""
                CREATE TABLE IF NOT EXISTS users (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    name TEXT NOT NULL,
                    email TEXT NOT NULL UNIQUE,
                    password TEXT NOT NULL,
                    role TEXT NOT NULL
                );
            """);

            stmt.execute("""
                CREATE TABLE IF NOT EXISTS subscriptions (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    user_id INTEGER NOT NULL,
                    type TEXT NOT NULL,
                    user_type TEXT NOT NULL,
                    base_price REAL NOT NULL,
                    final_price REAL NOT NULL,
                    start_date TEXT NOT NULL,
                    end_date TEXT NOT NULL,
                    status TEXT NOT NULL,
                    FOREIGN KEY(user_id) REFERENCES users(id)
                );
            """);

            stmt.execute("""
                CREATE TABLE IF NOT EXISTS reservations (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    user_id INTEGER NOT NULL,
                    date TEXT NOT NULL,
                    time TEXT NOT NULL,
                    note TEXT,
                    FOREIGN KEY(user_id) REFERENCES users(id)
                );
            """);

            stmt.execute("""
                CREATE TABLE IF NOT EXISTS fitness_goals (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    user_id INTEGER NOT NULL,
                    current_weight REAL NOT NULL,
                    target_weight REAL NOT NULL,
                    height REAL NOT NULL,
                    goal_type TEXT NOT NULL,
                    activity_level TEXT NOT NULL,
                    notes TEXT,
                    created_at TEXT NOT NULL,
                    FOREIGN KEY(user_id) REFERENCES users(id)
                );
            """);
            
            stmt.execute("""
            	    CREATE TABLE IF NOT EXISTS diet_plans (
            	        id INTEGER PRIMARY KEY AUTOINCREMENT,
            	        user_id INTEGER NOT NULL,
            	        calories REAL NOT NULL,
            	        plan_text TEXT NOT NULL,
            	        created_at TEXT NOT NULL,
            	        FOREIGN KEY(user_id) REFERENCES users(id)
            	    );
            	""");

        }
    }
}
