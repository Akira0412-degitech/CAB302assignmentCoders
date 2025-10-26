package com.example.cab302a1.model;

import java.sql.Timestamp;

/**
 * Represents a teacher user in the system.
 * <p>
 * This class extends {@link User} and inherits all its properties and functionality.
 * It is used to distinguish teacher accounts from other user roles such as students.
 * </p>
 */
public class Teacher extends User {

    /**
     * Creates a new teacher instance with the specified user details.
     *
     * @param _user_id    the unique ID of the teacher
     * @param _username   the teacher's username
     * @param _email      the teacher's email address
     * @param _role       the user's role (typically "Teacher")
     * @param _created_at the timestamp when the teacher account was created
     */
    public Teacher(int _user_id, String _username, String _email, String _role, Timestamp _created_at) {
        super(_user_id, _username, _email, _role, _created_at);
    }
}
