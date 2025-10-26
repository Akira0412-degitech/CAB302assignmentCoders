package com.example.cab302a1.model;

import java.sql.Timestamp;

/**
 * Represents a student user in the system.
 * <p>
 * This class extends {@link User} and inherits all its properties and behaviors.
 * It is primarily used to distinguish student users from other roles such as teachers.
 * </p>
 */
public class Student extends User {

    /**
     * Creates a new student instance with the specified user details.
     *
     * @param _user_id    the unique ID of the student
     * @param _username   the student's username
     * @param _email      the student's email address
     * @param _role       the user's role (typically "Student")
     * @param _created_at the timestamp when the student account was created
     */
    public Student(int _user_id, String _username, String _email, String _role, Timestamp _created_at) {
        super(_user_id, _username, _email, _role, _created_at);
    }
}
