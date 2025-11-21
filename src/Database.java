import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Database helper using SQLite.
 * All methods declare throws Exception (no try/catch inside).
 */
public class Database {

    private static final String URL = "jdbc:sqlite:electricity.db";

    // Constructor: ensure tables exist (throws Exception on error)
    public Database() throws Exception {
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {

            stmt.execute(
                    "CREATE TABLE IF NOT EXISTS homes (" +
                            "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                            "name TEXT NOT NULL UNIQUE" +
                            ");"
            );

            stmt.execute(
                    "CREATE TABLE IF NOT EXISTS devices (" +
                            "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                            "homeId INTEGER NOT NULL," +
                            "name TEXT NOT NULL," +
                            "watts REAL NOT NULL," +
                            "hours REAL NOT NULL," +
                            "FOREIGN KEY(homeId) REFERENCES homes(id) ON DELETE CASCADE" +
                            ");"
            );
        } // resources auto-closed; exceptions propagate
    }

    // simple connection getter (throws SQLException)
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL);
    }

    // Insert a home, return generated id
    public int insertHome(String name) throws Exception {
        String sql = "INSERT INTO homes(name) VALUES(?)";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, name);
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) return rs.getInt(1);
            }
        }
        return -1;
    }

    // Get all homes
    public List<Home> getAllHomes() throws Exception {
        List<Home> list = new ArrayList<>();
        String sql = "SELECT id, name FROM homes ORDER BY id";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                list.add(new Home(id, name));
            }
        }
        return list;
    }

    // Insert device for given homeId, return generated id
    public int insertDevice(int homeId, String name, double watts, double hours) throws Exception {
        String sql = "INSERT INTO devices(homeId, name, watts, hours) VALUES(?,?,?,?)";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, homeId);
            ps.setString(2, name);
            ps.setDouble(3, watts);
            ps.setDouble(4, hours);
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) return rs.getInt(1);
            }
        }
        return -1;
    }

    // Get devices for a home
    public List<Device> getDevicesByHomeId(int homeId) throws Exception {
        List<Device> list = new ArrayList<>();
        String sql = "SELECT id, name, watts, hours FROM devices WHERE homeId = ? ORDER BY id";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, homeId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    int id = rs.getInt("id");
                    String name = rs.getString("name");
                    double watts = rs.getDouble("watts");
                    double hours = rs.getDouble("hours");
                    list.add(new Device(id, homeId, name, watts, hours));
                }
            }
        }
        return list;
    }
}
