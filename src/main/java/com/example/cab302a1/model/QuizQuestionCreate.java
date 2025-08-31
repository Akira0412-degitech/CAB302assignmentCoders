package com.example.cab302a1.model;

import java.util.ArrayList;
import java.util.List;


public class QuizQuestionCreate {
    private String text;
    private final List<QuizChoiceCreate> choices = new ArrayList<>();

    public String getText() { return text; }
    public void setText(String text) { this.text = text; }


    public List<Choice> getChoices() { return choices; }

}
