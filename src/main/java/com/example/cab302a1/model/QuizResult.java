package com.example.cab302a1.model;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class QuizResult {

    private final StringProperty studentName;
    private final StringProperty quizName;
    private final IntegerProperty score;
    private final IntegerProperty totalQuestion;


    // Constructor for teacher view (with student name)
    public QuizResult(String studentName, String quizName, Integer score, Integer totalQuestion) {
        this.studentName = new SimpleStringProperty(studentName);
        this.quizName = new SimpleStringProperty(quizName);
        this.score = new SimpleIntegerProperty(score);
        this.totalQuestion = new SimpleIntegerProperty(totalQuestion);
    }

    // Constructor for student view (no student name needed)
    public QuizResult(String quizName, Integer score, Integer totalQuestion) {
        this.studentName = new SimpleStringProperty(""); // empty for students
        this.quizName = new SimpleStringProperty(quizName);
        this.score = new SimpleIntegerProperty(score);
        this.totalQuestion = new SimpleIntegerProperty(totalQuestion);
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

    public int getScore() {
        return score.get();
    }

    public IntegerProperty scoreProperty() {
        return score;
    }
}
