package com.example.cab302a1.dao;

import com.example.cab302a1.DBconnection;
import com.example.cab302a1.SignUp.SignUpController;
import com.example.cab302a1.model.Student;
import com.example.cab302a1.model.Teacher;
import com.example.cab302a1.model.User;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javax.sound.sampled.Control;
import java.sql.*;

public class UserDao {

    public User getUserById(int _userId) {
        String sql = "SELECT * FROM users WHERE user_id = ?";
        try (Connection conn = DBconnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, _userId);

            try(ResultSet rs = pstmt.executeQuery()){
                if(rs.next()){
                    String role = rs.getString("role");
                    if ("Teacher".equals(role)) {
                        return new Teacher(
                                rs.getInt("user_id"),
                                rs.getString("email"),
                                rs.getString("password"),
                                role,
                                rs.getTimestamp("created_at")
                        );
                    } else {
                        return new Student(
                                rs.getInt("user_id"),
                                rs.getString("email"),
                                rs.getString("password"),
                                role,
                                rs.getTimestamp("created_at")
                        );
                    }
                }
            }
        }catch (SQLException e ){
            e.printStackTrace();
        }

        return null;
    }

    public void printAllUsers() {
        String sql = "SELECT user_id, email, password, created_at, role FROM users";
        try (Connection conn = DBconnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                System.out.println(
                        rs.getInt("user_id") + " | " +
                                rs.getString("email") + " | " +
                                rs.getTimestamp("created_at") + " | " +
                                rs.getString("password") + " | " +
                                rs.getString("role")
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

    public User signUpUser(String _email, String _password, String _role){
        if(existsByEmail(_email)){
            System.out.printf("User already exists: " + _email);
            return null;
        }
        String sql = "INSERT INTO users(email, password, role) VALUES (? , ?, ?)";
        try(Connection conn = DBconnection.getConnection();
        PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, _email);
            pstmt.setString(2, _password);
            pstmt.setString(3, _role);

            int affectedRows = pstmt.executeUpdate();

            if(affectedRows > 0){
                try(ResultSet rs = pstmt.getGeneratedKeys()){
                    if(rs.next()){
                        int newId = rs.getInt(1);
                        User currentUser = getUserById(newId);
                        return currentUser;
                    }
                }
            }else {
                System.out.println("Sign-up User failed.");
                return null;
            }
            System.out.printf("User: " + _email + " Password: " + _password + " Role: " + _role + " Added");

        } catch (SQLException e){
            e.printStackTrace();
        }
        return null;
    }
    public User login(String _email, String _password){
        if(!existsByEmail(_email)){
            System.out.printf("User not found: " + _email);
            return null;
        }
        String sql = "SELECT * FROM users WHERE email = ? AND password = ? LIMIT 1";
        try {Connection conn = DBconnection.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, _email);
            pstmt.setString(2, _password);

            try(ResultSet rs = pstmt.executeQuery()){
                if(rs.next()){
                    String role = rs.getString("role");

                    if("Teacher".equals(role)){
                        return new Teacher(
                                rs.getInt("user_id"),
                                rs.getString("email"),
                                rs.getString("password"),
                                role,
                                rs.getTimestamp("created_at")
                        );
                    }else if("Student".equals(role)){
                        return new Student(
                                rs.getInt("user_id"),
                                rs.getString("email"),
                                rs.getString("password"),
                                role,
                                rs.getTimestamp("created_at")
                        );
                    }
                }

            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return null;
    }



}
