package com.example.cab302a1.model;

/**
 * Static class to hold the currently logged-in user's information.
 * This should be set immediately after a successful login.
 */
public class SessionManager {
    private static User currentUser;

    /**
     * Sets the user object upon successful login.
     * @param user The logged-in User object (Student or Teacher).
     */
    public static void setCurrentUser(User user) {
        currentUser = user;
        if (user != null) {
            System.out.println("Session started for user: " + user.getUsername() + " (ID: " + user.getUser_id() + ")");
        } else {
            System.out.println("Session ended (user set to null).");
        }
    }

    /**
     * Gets the currently logged-in user.
     * @return The current User object, or null if no one is logged in.
     */
    public static User getCurrentUser() {
        return currentUser;
    }
}