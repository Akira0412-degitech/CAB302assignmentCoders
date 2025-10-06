package com.example.cab302a1.shared;

import com.example.cab302a1.navigation.FxQuizNavigator;
import com.example.cab302a1.navigation.QuizNavigator;

// Minimal service locator for controllers (Refactoring)
public final class ServiceRegistry {
    private static QuizNavigator navigator;

    private ServiceRegistry() {}

    public static QuizNavigator nav() {
        if (navigator == null) navigator = new FxQuizNavigator();
        return navigator;
    }
}
