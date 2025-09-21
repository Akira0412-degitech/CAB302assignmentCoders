package com.example.cab302a1.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class QuizResult {
    private final StringProperty quizName;
    private final StringProperty score;

    public QuizResult(String quizName, String score) {
        this.quizName = new SimpleStringProperty(quizName);
        this.score = new SimpleStringProperty(score);
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
