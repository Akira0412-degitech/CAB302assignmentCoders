//not in service
package com.example.cab302a1.model;

import java.util.ArrayList;
import java.util.List;


public class Quiz {
    private String title;
    private String description;
    private final List<QuizQuestionCreate> questions = new ArrayList<>();

    public <E> Quiz(String title, String s, List<E> q1) {
    }

    public Quiz() {

    }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public List<QuizQuestionCreate> getQuestions() { return questions; }
}
