package com.example.cab302a1.dao;

import com.example.cab302a1.DBconnection;
import java.sql.*;

public class UserDao {
    // Simple method: print all users from DB
    public void printAllUsers() {
        String sql = "SELECT id, email, created_at FROM users";
        try (Connection conn = DBconnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                System.out.println(
                        rs.getInt("id") + " | " +
                                rs.getString("email") + " | " +
                                rs.getTimestamp("created_at")
                );
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
