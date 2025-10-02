package com.example.cab302a1.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.property.SimpleIntegerProperty; // Not used for score/total, but good to know

/**
 * Data model for displaying quiz review information in TableViews.
 */
public class QuizReview {
    private final int attemptId;
    private final int quizId;
    private final int userId;

    private final StringProperty quizName;
    private final int score; // Stored as a primitive int
    private final int total; // Stored as a primitive int
    private final String feedback; // Stored as a primitive String

    /**
     * FULL Constructor used by ReviewDao to load data (Primary constructor).
     */
    public QuizReview(int attemptId, int quizId, int userId, String quizName, int score, int total, String feedback) {
        this.attemptId = attemptId;
        this.quizId = quizId;
        this.userId = userId;
        this.quizName = new SimpleStringProperty(quizName);
        this.score = score;
        this.total = total;
        this.feedback = feedback;
    }

    /**
     * Convenience Constructor for simpler mock data (5 parameters).
     * Chains to the primary constructor using default values (0) for missing IDs.
     * * NOTE: Parameters must match what your test or calling code is using.
     * Assuming 5 parameters are: (quizName, score, total, feedback, quizId)
     */
    public QuizReview(String quizName, int score, int total, String feedback, int quizId) {
        // Calls the 7-parameter constructor: attemptId=0, quizId=passed_quizId, userId=0
        // We pass the 5 available values, and 0 for the two missing IDs (attemptId, userId).
        this(0, quizId, 0, quizName, score, total, feedback);
    }

    // --- Getters for Navigation ---

    public int getQuizId() {
        return quizId;
    }

    public int getUserId() {
        return userId;
    }

    public int getAttemptId() {
        return attemptId;
    }

    // --- Existing Getters and Properties ---

    public String getQuizName() {
        return quizName.get();
    }

    public StringProperty quizNameProperty() {
        return quizName;
    }

    // Returns score in "X/Y" format for the TableView
    public String getScoreSummary() {
        return score + "/" + total;
    }

    public StringProperty scoreSummaryProperty() {
        return new SimpleStringProperty(score + "/" + total);
    }

    public int getScore() {
        return score;
    }

    public int getTotal() {
        return total;
    }

    public String getFeedback() {
        return feedback;
    }
}