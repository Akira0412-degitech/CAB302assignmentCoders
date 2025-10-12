package com.example.cab302a1.dao.jdbc;

import com.example.cab302a1.DBconnection;
import com.example.cab302a1.dao.UserDao;
import com.example.cab302a1.model.Student;
import com.example.cab302a1.model.Teacher;
import com.example.cab302a1.model.User;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * {@code JdbcUserDao} provides a JDBC-based implementation of the {@link UserDao} interface.
 * <p>
 * This class handles CRUD and authentication operations for user records
 * stored in the {@code users} table, including registration, login,
 * and retrieval of users by ID or role.
 * </p>
 * <p>
 * Passwords are securely hashed using the {@link BCrypt} algorithm
 * before being stored in the database.
 * </p>
 */

public class JdbcUserDao implements UserDao {

    @Override
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
                                rs.getString("username"),
                                rs.getString("email"),
                                role,
                                rs.getTimestamp("created_at")
                        );
                    } else {
                        return new Student(
                                rs.getInt("user_id"),
                                rs.getString("username"),
                                rs.getString("email"),
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

    /**
     * {@inheritDoc}
     * <p>
     * Retrieves all users whose {@code role} is {@code 'Student'}.
     * This method is typically used by teachers to display
     * student lists in review or grading interfaces.
     * </p>
     */
    @Override
    public List<User> getAllStudents() {
        List<User> students = new ArrayList<>();
        // Select all fields needed by the Student constructor
        String sql = "SELECT user_id, username, email, role, created_at FROM users WHERE role = 'Student'";

        try (Connection conn = DBconnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                // Create Student objects from the results
                students.add(new Student(
                        rs.getInt("user_id"),
                        rs.getString("username"),
                        rs.getString("email"),
                        rs.getString("role"),
                        rs.getTimestamp("created_at")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return students;
    }

    //for debugging
    /**
     * Prints all users for debugging or administrative inspection.
     * <p>
     * This method is not intended for production use.
     * </p>
     */
    @Override
    public void printAllUsers() {
        String sql = "SELECT user_id, username, email, password, created_at, role FROM users";
        try (Connection conn = DBconnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                System.out.println(
                        rs.getInt("user_id") + " | " +
                                rs.getString("username") + " | " +
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

    /**
     * {@inheritDoc}
     * <p>
     * Checks if a user record exists with the given email.
     * Executes a {@code SELECT 1} query with a limit for performance.
     * </p>
     */
    @Override
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

    /**
     * {@inheritDoc}
     * <p>
     * Registers a new user with a hashed password using {@link BCrypt}.
     * Returns the created {@link User} object after successful insertion.
     * </p>
     */
    @Override
    public User signUpUser(String _username, String _email, String _password, String _role){
        if(existsByEmail(_email)){
            System.out.printf("User already exists: " + _email);
            return null;
        }
        String sql = "INSERT INTO users(username, email, password, role) VALUES (?, ?, ?, ?)";
        String hashedPassword = BCrypt.hashpw(_password, BCrypt.gensalt());

        try(Connection conn = DBconnection.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, _username);
            pstmt.setString(2, _email);
            pstmt.setString(3, hashedPassword);
            pstmt.setString(4, _role);

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
            System.out.printf("User: " + _username + " Email: " + _email + " Password: " + _password + " Role: " + _role + " Added");

        } catch (SQLException e){
            e.printStackTrace();
        }
        return null;
    }

    /**
     * {@inheritDoc}
     * <p>
     * Authenticates a user by verifying the entered password against the
     * hashed password stored in the database using {@link BCrypt#checkpw(String, String)}.
     * Returns a {@link Teacher} or {@link Student} object based on role.
     * </p>
     */
    @Override
    public User login(String _email, String _password){
        if(!existsByEmail(_email)){
            System.out.printf("User not found: " + _email);
            return null;
        }
        String sql = "SELECT * FROM users WHERE email = ?";
        try {Connection conn = DBconnection.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, _email);


            try(ResultSet rs = pstmt.executeQuery()){
                if(rs.next()){
                    String storedPassword = rs.getString("password");
                    if(BCrypt.checkpw(_password, storedPassword)){
                        String role = rs.getString("role");

                        if("Teacher".equals(role)){
                            return new Teacher(
                                    rs.getInt("user_id"),
                                    rs.getString("username"),
                                    rs.getString("email"),
                                    role,
                                    rs.getTimestamp("created_at")
                            );
                        }else if("Student".equals(role)){
                            return new Student(
                                    rs.getInt("user_id"),
                                    rs.getString("username"),
                                    rs.getString("email"),
                                    role,
                                    rs.getTimestamp("created_at")
                            );
                        }
                    }
                }
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return null;
    }


}
