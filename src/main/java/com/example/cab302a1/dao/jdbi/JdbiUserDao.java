package com.example.cab302a1.dao.jdbi;

import com.example.cab302a1.DBconnection;
import com.example.cab302a1.dao.UserDao;
import com.example.cab302a1.model.Student;
import com.example.cab302a1.model.Teacher;
import com.example.cab302a1.model.User;
import org.jdbi.v3.core.Jdbi;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.Timestamp;
import java.util.List;

/**
 * {@code JdbiUserDao} provides a JDBI-based implementation of the {@link UserDao} interface.
 * <p>
 * This class manages user CRUD and authentication using JDBI to simplify
 * database operations while retaining all logic from {@code JdbcUserDao}.
 * </p>
 */
public class JdbiUserDao implements UserDao {

    private final Jdbi jdbi;

    public JdbiUserDao() {
        this.jdbi = DBconnection.getJdbi();
    }

    /**
     * {@inheritDoc}
     * <p>
     * Retrieves a user by ID and returns either a {@link Teacher} or {@link Student}.
     * </p>
     */
    @Override
    public User getUserById(int _userId) {
        String sql = "SELECT * FROM users WHERE user_id = :userId";

        return jdbi.withHandle(handle ->
                handle.createQuery(sql)
                        .bind("userId", _userId)
                        .map((rs, ctx) -> {
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
                        })
                        .findOne()
                        .orElse(null)
        );
    }

    /**
     * {@inheritDoc}
     * <p>
     * Retrieves all users with the role 'Student'.
     * </p>
     */
    @Override
    public List<User> getAllStudents() {
        String sql = """
            SELECT user_id, username, email, role, created_at
            FROM users
            WHERE role = 'Student'
        """;

        return jdbi.withHandle(handle ->
                handle.createQuery(sql)
                        .map((rs, ctx) -> new User(
                                rs.getInt("user_id"),
                                rs.getString("username"),
                                rs.getString("email"),
                                rs.getString("role"),
                                rs.getTimestamp("created_at")
                        ))
                        .list()
        );
    }

    /**
     * Prints all users for debugging or admin inspection.
     */
    @Override
    public void printAllUsers() {
        String sql = "SELECT user_id, username, email, password, created_at, role FROM users";

        jdbi.useHandle(handle ->
                handle.createQuery(sql)
                        .map((rs, ctx) -> {
                            System.out.println(
                                    rs.getInt("user_id") + " | " +
                                            rs.getString("username") + " | " +
                                            rs.getString("email") + " | " +
                                            rs.getTimestamp("created_at") + " | " +
                                            rs.getString("password") + " | " +
                                            rs.getString("role")
                            );
                            return null;
                        })
                        .list()
        );
    }

    /**
     * {@inheritDoc}
     * <p>
     * Checks if a user with the given email already exists.
     * </p>
     */
    @Override
    public boolean existsByEmail(String _email) {
        String sql = "SELECT 1 FROM users WHERE email = :email LIMIT 1";

        return jdbi.withHandle(handle ->
                handle.createQuery(sql)
                        .bind("email", _email)
                        .mapTo(Integer.class)
                        .findOne()
                        .isPresent()
        );
    }

    /**
     * {@inheritDoc}
     * <p>
     * Registers a new user with a hashed password using BCrypt.
     * </p>
     */
    @Override
    public User signUpUser(String _username, String _email, String _password, String _role) {
        if (existsByEmail(_email)) {
            System.out.println("User already exists: " + _email);
            return null;
        }

        String sql = """
            INSERT INTO users (username, email, password, role)
            VALUES (:username, :email, :password, :role)
        """;

        String hashedPassword = BCrypt.hashpw(_password, BCrypt.gensalt());

        Integer newId = jdbi.withHandle(handle ->
                handle.createUpdate(sql)
                        .bind("username", _username)
                        .bind("email", _email)
                        .bind("password", hashedPassword)
                        .bind("role", _role)
                        .executeAndReturnGeneratedKeys("user_id")
                        .mapTo(Integer.class)
                        .findOne()
                        .orElse(null)
        );

        if (newId == null) {
            System.out.println("Sign-up failed for: " + _email);
            return null;
        }

        User currentUser = getUserById(newId);
        System.out.printf("User: %s | Email: %s | Role: %s added%n", _username, _email, _role);
        return currentUser;
    }

    /**
     * {@inheritDoc}
     * <p>
     * Authenticates a user by comparing plaintext vs hashed password using BCrypt.
     * </p>
     */
    @Override
    public User login(String _email, String _password) {
        if (!existsByEmail(_email)) {
            System.out.println("User not found: " + _email);
            return null;
        }

        String sql = "SELECT * FROM users WHERE email = :email";

        return jdbi.withHandle(handle ->
                handle.createQuery(sql)
                        .bind("email", _email)
                        .map((rs, ctx) -> {
                            String storedPassword = rs.getString("password");
                            if (BCrypt.checkpw(_password, storedPassword)) {
                                String role = rs.getString("role");
                                Timestamp createdAt = rs.getTimestamp("created_at");

                                if ("Teacher".equals(role)) {
                                    return new Teacher(
                                            rs.getInt("user_id"),
                                            rs.getString("username"),
                                            rs.getString("email"),
                                            role,
                                            createdAt
                                    );
                                } else if ("Student".equals(role)) {
                                    return new Student(
                                            rs.getInt("user_id"),
                                            rs.getString("username"),
                                            rs.getString("email"),
                                            role,
                                            createdAt
                                    );
                                }
                            }
                            return null;
                        })
                        .findOne()
                        .orElse(null)
        );
    }
}
