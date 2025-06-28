package Bus;

import java.sql.*;

public class DBConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/busbookingdb";
    private static final String USER = "root";
    private static final String PASSWORD = "Swetha3123!";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}

