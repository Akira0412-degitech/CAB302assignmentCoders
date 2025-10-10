package com.example.cab302a1.dao;

/**
 * AttemptDao defines the contract for managing
 * quiz attempt operations such as starting, ending,
 * and retrieving quiz attempt data.
 */
public interface AttemptDao {

    /**
     * Start a new quiz attempt for a specific user and quiz.
     * @param quizId The quiz ID
     * @param userId The user ID
     * @return The generated attempt ID, or -1 if failed
     */
    int startAttempt(int quizId, int userId);

    /**
     * Check if an attempt record exists.
     * @param attemptId The attempt ID
     * @return true if exists, false otherwise
     */
    boolean attemptExist(int attemptId);

    /**
     * Mark a quiz attempt as completed and calculate score.
     * @param attemptId The attempt ID
     */
    void endAttempt(int attemptId);

    /**
     * Get the most recent completed attempt score for a quiz.
     * @param quizId The quiz ID
     * @param userId The user ID
     * @return The score value or null if none found
     */
    Integer getScore(int quizId, int userId);

    /**
     * Check if a user has completed a specific quiz.
     * @param quizId The quiz ID
     * @param userId The user ID
     * @return true if completed, false otherwise
     */
    boolean hasCompleted(int quizId, int userId);

    /**
     * Update teacher feedback for a quiz attempt.
     * @param attemptId The attempt ID
     * @param feedback Feedback message
     */
    void updateFeedback(int attemptId, String feedback);

    /**
     * Retrieve the most recent attempt ID for a given quiz and user.
     * @param quizId The quiz ID
     * @param userId The user ID
     * @return The attempt ID or null if not found
     */
    Integer getAttemptId(int quizId, int userId);
}
