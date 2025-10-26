package com.example.cab302a1.model;

import java.sql.Timestamp;

/**
 * Represents a user entity within the system.
 * <p>
 * This model stores essential user information including identifiers, credentials,
 * roles, and account creation timestamps. It is commonly used when handling authentication,
 * authorization, and profile-related operations.
 * </p>
 */
public class User {
    protected int user_id;
    protected String username;
    protected String email;
    protected String password;
    protected String role;
    protected Timestamp created_at;

    /**
     * Creates a user instance with the specified details.
     *
     * @param _user_id    the unique ID of the user
     * @param _username   the username chosen by the user
     * @param _email      the user's email address
     * @param _role       the role assigned to the user (e.g., "Teacher" or "Student")
     * @param _created_at the timestamp indicating when the user account was created
     */
    public User(int _user_id, String _username, String _email, String _role, Timestamp _created_at) {
        this.user_id = _user_id;
        this.username = _username;
        this.email = _email;
        this.role = _role;
        this.created_at = _created_at;
    }

    /**
     * Returns the user's unique ID.
     * @return the user ID
     */
    public int getUser_id() { return user_id; }

    /**
     * Returns the username of the user.
     * @return the username
     */
    public String getUsername() { return username; }

    /**
     * Returns the email address of the user.
     * @return the email address
     */
    public String getEmail() { return email; }

    /**
     * Returns the role assigned to the user.
     * @return the user's role
     */
    public String getRole() { return role; }

    /**
     * Returns the timestamp of when the user account was created.
     * @return the account creation timestamp
     */
    public Timestamp getCreated_at() { return created_at; }
}
