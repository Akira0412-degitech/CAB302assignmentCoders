package com.example.cab302a1.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * Represents a quiz review entry, used for displaying completed quiz attempts in TableViews.
 * <p>
 * Each instance stores summary data such as quiz name, score, total questions, and feedback.
 * It also provides JavaFX {@link StringProperty} bindings to support UI display in table columns.
 * </p>
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
     * Creates a quiz review record for a real attempt retrieved from the database.
     * <p>
     * Typically used by DAO classes (e.g., ReviewDao) when loading quiz attempts
     * and displaying them in the review table.
     * </p>
     *
     * @param attemptId the unique ID of the quiz attempt
     * @param quizId    the quiz ID associated with the attempt
     * @param userId    the user ID who took the quiz
     * @param quizName  the name of the quiz
     * @param score     the number of correct answers
     * @param total     the total number of questions
     * @param feedback  the feedback text for the attempt
     */
    public QuizReview(int attemptId, int quizId, int userId,
                      String quizName, int score, int total, String feedback) {
        this.attemptId = attemptId;
        this.quizId = quizId;
        this.userId = userId;
        this.quizName = new SimpleStringProperty(quizName);
        this.score = score;
        this.total = total;
        this.feedback = feedback;
    }

    /**
     * Creates a quiz review record for mock or demo data.
     * <p>
     * This constructor omits the attempt and user IDs to prevent confusion with
     * real database entries.
     * </p>
     *
     * @param quizName the quiz name
     * @param score    the number of correct answers
     * @param total    the total number of questions
     * @param feedback the feedback text
     * @param quizId   the quiz ID
     */
    public QuizReview(String quizName, int score, int total, String feedback, int quizId) {
        this(0, quizId, 0, quizName, score, total, feedback);
    }

    // --- Getters for Navigation ---

    /**
     * Returns the quiz ID.
     * @return the quiz ID
     */
    public int getQuizId() {
        return quizId;
    }

    /**
     * Returns the user ID of the person who took the quiz.
     * @return the user ID
     */
    public int getUserId() {
        return userId;
    }

    /**
     * Returns the attempt ID.
     * @return the attempt ID
     */
    public int getAttemptId() {
        return attemptId;
    }

    // --- Existing Getters and Properties ---

    /**
     * Returns the quiz name.
     * @return the quiz name
     */
    public String getQuizName() {
        return quizName.get();
    }

    /**
     * Returns a property representing the quiz name.
     * <p>
     * Used for JavaFX data binding in {@code TableView} columns.
     * </p>
     *
     * @return a {@link StringProperty} representing the quiz name
     */
    public StringProperty quizNameProperty() {
        return quizName;
    }

    /**
     * Returns the score summary in "X/Y" format (e.g., "8/10").
     * <p>
     * This is used for displaying concise score information in the UI.
     * </p>
     *
     * @return a formatted score summary string
     */
    public String getScoreSummary() {
        return score + "/" + total;
    }

    /**
     * Returns a property representing the score summary ("X/Y" format).
     * <p>
     * This allows binding the score summary directly to JavaFX TableView columns.
     * </p>
     *
     * @return a {@link StringProperty} containing the score summary
     */
    public StringProperty scoreSummaryProperty() {
        return new SimpleStringProperty(getScoreSummary());
    }

    /**
     * Returns the number of correct answers.
     * @return the score
     */
    public int getScore() {
        return score;
    }

    /**
     * Returns the total number of questions in the quiz.
     * @return the total question count
     */
    public int getTotal() {
        return total;
    }

    /**
     * Returns the feedback provided for this quiz attempt.
     * @return the feedback text
     */
    public String getFeedback() {
        return feedback;
    }
}
