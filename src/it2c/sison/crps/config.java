package it2c.sison.crps;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class config {
    //Connection Method to SQLITE

    public static Connection connectDB() {
        Connection con = null;
        try {
            Class.forName("org.sqlite.JDBC"); // Load the SQLite JDBC driver
            con = DriverManager.getConnection("jdbc:sqlite:crps.db"); // Establish connection
            System.out.println("Connection Successful");
        } catch (Exception e) {
            System.out.println("Connection Failed: " + e);
        }
        return con;
    }
    
    public void addTenants(String sql, String... values) {
        try (Connection conn = this.connectDB(
        );
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            for (int i = 0; i < values.length; i++) {
                pstmt.setString(i + 1, values[i]);
            }
            
            pstmt.executeUpdate();
            System.out.println("Record added successfully!");
        } catch (SQLException e) {
            System.out.println("Error adding record: " + e.getMessage());
        } 
    }
    
}
