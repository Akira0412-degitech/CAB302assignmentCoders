package com.example.cab302a1.util;

import com.example.cab302a1.model.User;

public class Session {
    private static User currentUser;

    public static void setCurrentUser(User _user){
        currentUser = _user;
    }
    public static User getCurrentUser(){
        return currentUser;
    }
    public static void clearUser(){
        currentUser = null;
    }

    public static boolean isLoggedaIn(){
        return currentUser != null;
    }

    public static boolean isTeacher(){
        return currentUser != null && currentUser.getRole().equals("Teacher") ;
    }
    public static boolean isStudent(){
        return currentUser != null && currentUser.getRole().equals("Student");
    }

    // New method to get the user's role
    public static String getCurrentUserRole() {
        if (currentUser != null) {
            return currentUser.getRole();
        }
        return ""; // Return an empty string if no user is logged in
    }
}
