package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {

    private static final String URL =
        "jdbc:mysql://localhost:3306/reservation_salles"
        + "?useSSL=false&serverTimezone=UTC"
        + "&allowPublicKeyRetrieval=true"
        + "&characterEncoding=UTF-8";

    private static final String USER     = "root";
    private static final String PASSWORD = ""; // ton mot de passe MySQL

    private static Connection connection = null;

    private DBConnection() {}

    public static Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
                connection = DriverManager.getConnection(URL, USER, PASSWORD);
            } catch (ClassNotFoundException e) {
                throw new SQLException("Driver introuvable : " + e.getMessage());
            }
        }
        return connection;
    }

    public static void fermer() {
        if (connection != null) {
            try { connection.close(); }
            catch (SQLException e) { e.printStackTrace(); }
        }
    }
}