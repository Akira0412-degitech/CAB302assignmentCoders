package com.example.cab302a1.dao;

import com.example.cab302a1.model.Student;
import com.example.cab302a1.model.User;
import java.util.List;

/**
 * UserDao defines common database operations
 * for managing user accounts such as registration,
 * login, and retrieval by ID or role.
 *
 * It acts as a contract for different implementations
 * (e.g., JdbcUserDao).
 */
public interface UserDao {

    /**
     * Get a user by their unique ID.
     * @param userId the user's unique ID
     * @return the User object, or null if not found
     */
    User getUserById(int userId);

    /**
     * Retrieve all users with the role "Student".
     * @return list of students
     */
    List<Student> getAllStudents();

    void printAllUsers();
    /**
     * Check if a user exists by email.
     * @param email the email address to check
     * @return true if exists, false otherwise
     */
    boolean existsByEmail(String email);

    /**
     * Register a new user with hashed password.
     * @param username username
     * @param email email
     * @param password plain text password
     * @param role user role (Teacher or Student)
     * @return created User object, or null if failed
     */
    User signUpUser(String username, String email, String password, String role);

    /**
     * Log in an existing user with email and password.
     * @param email user email
     * @param password plain text password
     * @return logged-in User object, or null if failed
     */
    User login(String email, String password);
}
