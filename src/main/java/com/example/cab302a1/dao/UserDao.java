package com.example.cab302a1.dao;

import com.example.cab302a1.DBconnection;
import java.sql.*;

public class UserDao {
    // Simple method: print all users from DB



    public void printAllUsers() {
        String sql = "SELECT id, email, created_at, role FROM users";
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

    public boolean existsByEmail(String _email){
        String sql = "SELECT 1 FROM users where email = ? LIMIT 1";

        try(Connection conn = DBconnection.getConnection();
        PreparedStatement pstmt = conn.prepareStatement(sql))
        {
            pstmt.setString(1, _email);
            try(ResultSet rs = pstmt.executeQuery()){
                return rs.next();
            }

        } catch (SQLException e ){
            e.printStackTrace();
        }
        return false;
    }

    public boolean signUpUser(String _email, String _password){
        if(existsByEmail(_email)){
            System.out.printf("User already exists: " + _email);
            return false;
        }
        String sql = "INSERT INTO users(email, password) VALUES (? , ?)";
        try(Connection conn = DBconnection.getConnection();
        PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, _email);
            pstmt.setString(2, _password);
            pstmt.executeUpdate();
            System.out.printf("User" + _email + "Passwod: " + _password + "Added");
            return true;
        } catch (SQLException e){
            e.printStackTrace();
        }
        return false;
    }
    public boolean login(String _email, String _password){
        if(!existsByEmail(_email)){
            System.out.printf("User not found: " + _email);
            return false;
        }
        String sql = "SELECT password FROM users where email = ? LIMIT 1";
        try {Connection conn = DBconnection.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, _email);

            try(ResultSet rs = pstmt.executeQuery()){
                if(rs.next()){
                    String password = rs.getString("password");
                    if(password.equals(_password)){
                        System.out.printf("Login successful for user: " + _email);
                        return  true;
                    }
                }

            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return false;
    }

}
