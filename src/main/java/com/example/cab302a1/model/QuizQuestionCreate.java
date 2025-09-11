package com.example.cab302a1.model;

import java.util.ArrayList;
import java.util.List;


public class QuizQuestionCreate {
    private String questionText;
    private final List<QuizChoiceCreate> choices = new ArrayList<>();

    public String getQuestionText() { return questionText; }
    public void setQuestionText(String questionText) { this.questionText = questionText; }


    public List<QuizChoiceCreate> getChoices() { return choices; }

}
