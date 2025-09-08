package com.example.cab302a1.model;

public class QuizChoiceCreate {
    private String text;
    private boolean correct;

    public QuizChoiceCreate() {} //constructor
    public QuizChoiceCreate(String text, boolean correct) {
        this.text = text; // put the value recieved from cstor into the variable called
        this.correct = correct;
    }

    public String getText() { return text; } //extract selected text
    public void setText(String text) { this.text = text; } //change text to settext

    public boolean isCorrect() { return correct; } // T/F check
    public void setCorrect(boolean correct) { this.correct = correct; }
}

