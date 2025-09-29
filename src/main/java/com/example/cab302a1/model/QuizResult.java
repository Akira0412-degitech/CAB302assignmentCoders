package com.example.cab302a1.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class QuizResult {

    private final StringProperty studentName;
    private final StringProperty quizName;
    private final StringProperty score;

    // Constructor for teacher view (with student name)
    public QuizResult(String studentName, String quizName, String score) {
        this.studentName = new SimpleStringProperty(studentName);
        this.quizName = new SimpleStringProperty(quizName);
        this.score = new SimpleStringProperty(score);
    }

    // Constructor for student view (no student name needed)
    public QuizResult(String quizName, String score) {
        this.studentName = new SimpleStringProperty(""); // empty for students
        this.quizName = new SimpleStringProperty(quizName);
        this.score = new SimpleStringProperty(score);
    }

    // Getters and properties
    public String getStudentName() {
        return studentName.get();
    }

    public StringProperty studentNameProperty() {
        return studentName;
    }

    public String getQuizName() {
        return quizName.get();
    }

    public StringProperty quizNameProperty() {
        return quizName;
    }

    public String getScore() {
        return score.get();
    }

    public StringProperty scoreProperty() {
        return score;
    }
}
