package com.example.cab302a1.model;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class QuizReview {

//    private final StringProperty studentName;
    private final StringProperty quizName;
    private final IntegerProperty score;
    private final IntegerProperty totalQuestion;
    private final StringProperty feedback;



//    // Constructor for teacher view (with student name)
//    public QuizReview(String studentName, String quizName, int score, int totalQuestion, String feedback) {
//        if(feedback == null || feedback.isEmpty()){
//            feedback = "No feedback";
//        }
//        this.studentName = new SimpleStringProperty(studentName);
//        this.quizName = new SimpleStringProperty(quizName);
//        this.score = new SimpleIntegerProperty(score);
//        this.totalQuestion = new SimpleIntegerProperty(totalQuestion);
//        this.feedback = new SimpleStringProperty(feedback);
//    }

    // Constructor for student view (no student name needed)
    public QuizReview(String quizName, int score, int totalQuestion, String feedback) {
        if(feedback == null || feedback.isEmpty()){
            feedback = "No feedback";
        }
//        this.studentName = new SimpleStringProperty(""); // empty for students
        this.quizName = new SimpleStringProperty(quizName);
        this.score = new SimpleIntegerProperty(score);
        this.totalQuestion = new SimpleIntegerProperty(totalQuestion);
        this.feedback = new SimpleStringProperty(feedback);
    }

    // Getters and properties

//    public String getStudentName() {
//        return studentName.get();
//    }
    public String getQuizName() {
        return quizName.get();
    }
    public int getScore() {
        return score.get();
    }
    public int getTotalQuestion() {
        return totalQuestion.get();
    }
    public String getFeedback() {
        return feedback.get();
    }

    public void setFeedback(String _feedbackText){
        if(_feedbackText == null || _feedbackText.isEmpty()){
            _feedbackText = "No feedback";
        }
        this.feedback.set(_feedbackText);
    }
//    public StringProperty studentNameProperty() {
//        return studentName;
//    }

    public StringProperty quizNameProperty() {
        return quizName;
    }

    public IntegerProperty scoreProperty() {
        return score;
    }

    public IntegerProperty totalQuestionProperty() {
        return totalQuestion;
    }

    public StringProperty feedbackProperty() {
        return feedback;
    }

    public StringProperty scoreSummaryProperty() {
        return new SimpleStringProperty(getScore() + "/" + getTotalQuestion());
    }


}
