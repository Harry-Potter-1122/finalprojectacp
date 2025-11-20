import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class Database {

    private static final String URL = "jdbc:sqlite:electricity.db";

    // Constructor auto-initializes the database
    public Database() throws Exception {
        Connection conn = getConnection();
        Statement stmt = conn.createStatement();

        stmt.execute("""
            CREATE TABLE IF NOT EXISTS homes (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                name TEXT NOT NULL UNIQUE
            )
        """);

        stmt.execute("""
            CREATE TABLE IF NOT EXISTS devices (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                homeName TEXT NOT NULL,
                deviceName TEXT NOT NULL,
                watts REAL NOT NULL,
                hours REAL NOT NULL
            )
        """);

        stmt.close();
        conn.close();
    }

    // Get database connection (no try/catch)
    public static Connection getConnection() throws SQLException {
        Connection conn = DriverManager.getConnection(URL);

        // Very basic "error checking" without exceptions:
        if (conn == null) {
            System.out.println("Database connection failed.");
        } else {
            System.out.println("Database connected.");
        }

        return conn;
    }
}
