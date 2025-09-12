//not in service
package com.example.cab302a1.model;

import java.util.ArrayList;
import java.util.List;


public class Quiz {
    private int quiz_id;
    private String title;
    private String description;
    private  int created_by;
    private final List<QuizQuestionCreate> questions = new ArrayList<>();

    public int getQuizId(){ return quiz_id ;}
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public int getCreated_by(){ return created_by;}
    public List<QuizQuestionCreate> getQuestions() { return questions; }

    public void setQuizId(Integer _quizId){this.quiz_id = _quizId;}
    public void setTitle(String title) { this.title = title; }
    public void setDescription(String description) { this.description = description; }
    public void setCreated_by(Integer _created_by){this.created_by = _created_by;}


    public void setQuestions(List<QuizQuestionCreate> qs) {
        this.questions.clear();
        if (qs != null) {
            this.questions.addAll(qs);
        }
    }
}
