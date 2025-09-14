package com.example.cab302a1.ui.policy;

import com.example.cab302a1.UserRole;

public final class QuizCardPolicy {
    private QuizCardPolicy() {}
    public static boolean showEditButton(UserRole role) {
        return role == UserRole.TEACHER;
    }
}
