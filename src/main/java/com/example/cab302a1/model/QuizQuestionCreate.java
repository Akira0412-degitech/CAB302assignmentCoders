package com.example.cab302a1.model;

import java.util.ArrayList;
import java.util.List;


public class QuizQuestionCreate {
    private String questionText;
    private final List<QuizChoiceCreate> choices = new ArrayList<>();

    public <E> QuizQuestionCreate(String s, List<E> es, String s1) {
    }

    public QuizQuestionCreate() {

    }

    public String getQuestionText() { return questionText; }
    public void setQuestionText(String questionText) { this.questionText = questionText; }


    public List<QuizChoiceCreate> getChoices() { return choices; }


    //explanation
    private String explanation;

    public String getExplanation() { return explanation; }
    public void setExplanation(String explanation) { this.explanation = explanation; }

}
