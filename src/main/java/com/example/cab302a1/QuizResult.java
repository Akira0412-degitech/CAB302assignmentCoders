package com.example.cab302a1;

/**
 * A simple class to model a quiz result.
 */
public class QuizResult {
    private String quizName;
    private String score;

    public QuizResult(String quizName, String score) {
        this.quizName = quizName;
        this.score = score;
    }

    public String getQuizName() {
        return quizName;
    }

    public String getScore() {
        return score;
    }
}
