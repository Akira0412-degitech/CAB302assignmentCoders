package com.example.cab302a1.result;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Data model for quiz result information.
 * This class encapsulates all data related to a completed quiz result,
 * providing a standardized way for team members to pass quiz data to the result page.
 * 
 * @author CAB302 Assignment Team
 * @version 1.0
 */
public class QuizResultData {
    
    private final int correctAnswers;
    private final int totalQuestions;
    private final String quizTitle;
    private final LocalDateTime completionTime;
    private final int quizId;
    private final int userId;
    
    /**
     * Constructor for creating quiz result data.
     * 
     * @param correctAnswers The number of correct answers
     * @param totalQuestions The total number of questions in the quiz
     * @param quizTitle The title/name of the completed quiz
     * @param quizId The unique identifier for the quiz
     * @param userId The unique identifier for the user
     */
    public QuizResultData(int correctAnswers, int totalQuestions, String quizTitle, int quizId, int userId) {
        if (correctAnswers < 0 || totalQuestions <= 0 || correctAnswers > totalQuestions) {
            throw new IllegalArgumentException("Invalid score values: correct=" + correctAnswers + ", total=" + totalQuestions);
        }
        if (quizTitle == null || quizTitle.trim().isEmpty()) {
            throw new IllegalArgumentException("Quiz title cannot be null or empty");
        }
        
        this.correctAnswers = correctAnswers;
        this.totalQuestions = totalQuestions;
        this.quizTitle = quizTitle.trim();
        this.quizId = quizId;
        this.userId = userId;
        this.completionTime = LocalDateTime.now();
    }
    
    /**
     * Simplified constructor for basic score display.
     * Uses default values for IDs and generates a generic title.
     * 
     * @param correctAnswers The number of correct answers
     * @param totalQuestions The total number of questions in the quiz
     */
    public QuizResultData(int correctAnswers, int totalQuestions) {
        this(correctAnswers, totalQuestions, "Quiz Completed", 0, 0);
    }
    
    // Getters
    
    /**
     * Gets the number of correct answers.
     * @return The number of correct answers
     */
    public int getCorrectAnswers() {
        return correctAnswers;
    }
    
    /**
     * Gets the total number of questions.
     * @return The total number of questions
     */
    public int getTotalQuestions() {
        return totalQuestions;
    }
    
    /**
     * Gets the quiz title.
     * @return The quiz title
     */
    public String getQuizTitle() {
        return quizTitle;
    }
    
    /**
     * Gets the completion time.
     * @return The completion time as LocalDateTime
     */
    public LocalDateTime getCompletionTime() {
        return completionTime;
    }
    
    /**
     * Gets the quiz ID.
     * @return The quiz ID
     */
    public int getQuizId() {
        return quizId;
    }
    
    /**
     * Gets the user ID.
     * @return The user ID
     */
    public int getUserId() {
        return userId;
    }
    
    // Computed properties
    
    /**
     * Gets the score as a formatted string (e.g., "13/20").
     * @return The score in "correct/total" format
     */
    public String getScoreString() {
        return correctAnswers + "/" + totalQuestions;
    }
    
    /**
     * Gets the score as a percentage.
     * @return The score percentage (0.0 to 100.0)
     */
    public double getScorePercentage() {
        return (double) correctAnswers / totalQuestions * 100.0;
    }
    
    /**
     * Determines if the score is considered a passing grade.
     * Uses 60% as the passing threshold.
     * @return true if the score is 60% or higher
     */
    public boolean isPassing() {
        return getScorePercentage() >= 60.0;
    }
    
    /**
     * Gets a letter grade based on the score percentage.
     * @return Letter grade (A, B, C, D, F)
     */
    public String getLetterGrade() {
        double percentage = getScorePercentage();
        if (percentage >= 90) return "A";
        if (percentage >= 80) return "B";
        if (percentage >= 70) return "C";
        if (percentage >= 60) return "D";
        return "F";
    }
    

    
    /**
     * Validation method to check if the quiz result data is valid.
     * @return true if all data is valid
     */
    public boolean isValid() {
        return correctAnswers >= 0 && 
               totalQuestions > 0 && 
               correctAnswers <= totalQuestions &&
               quizTitle != null && 
               !quizTitle.trim().isEmpty();
    }
    
    // Object methods
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        QuizResultData that = (QuizResultData) obj;
        return correctAnswers == that.correctAnswers &&
               totalQuestions == that.totalQuestions &&
               quizId == that.quizId &&
               userId == that.userId &&
               Objects.equals(quizTitle, that.quizTitle);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(correctAnswers, totalQuestions, quizTitle, quizId, userId);
    }
    
    @Override
    public String toString() {
        return String.format("QuizResultData{score=%s, title='%s', percentage=%.1f%%, grade=%s, completion=%s}", 
                           getScoreString(), quizTitle, getScorePercentage(), getLetterGrade(), completionTime);
    }
    
    /**
     * Creates a JSON-like string representation for debugging.
     * @return JSON-formatted string of the quiz result data
     */
    public String toJsonString() {
        return String.format("{\"correctAnswers\":%d,\"totalQuestions\":%d,\"quizTitle\":\"%s\",\"quizId\":%d,\"userId\":%d,\"scorePercentage\":%.2f,\"letterGrade\":\"%s\",\"completionTime\":\"%s\"}", 
                           correctAnswers, totalQuestions, quizTitle, quizId, userId, getScorePercentage(), getLetterGrade(), completionTime);
    }
}
