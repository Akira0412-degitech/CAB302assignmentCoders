//not in service
package com.example.cab302a1.model;

import java.util.ArrayList;
import java.util.List;


public class Quiz {
    private int quiz_id;
    private String title;
    private String description;
    private  int created_by;
    private boolean is_Hidden;

    private final List<QuizQuestionCreate> questions = new ArrayList<>();

    public Quiz(int quiz_id, String title, String description, int created_by, boolean is_Hidden) {
        this.quiz_id = quiz_id;
        this.title = title;
        this.description = description;
        this.created_by = created_by;
        this.is_Hidden = is_Hidden;
    }

    public Quiz(){

    }

    public int getQuizId(){ return quiz_id ;}
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public int getCreated_by(){ return created_by;}
    public boolean getIsHidden(){return is_Hidden;}


    public List<QuizQuestionCreate> getQuestions() { return questions; }

    public void setQuizId(Integer _quizId){this.quiz_id = _quizId;}
    public void setTitle(String title) { this.title = title; }
    public void setDescription(String description) { this.description = description; }
    public void setCreated_by(Integer _created_by){this.created_by = _created_by;}
    public void setIsHidden(boolean _is_Hidden){
        this.is_Hidden = _is_Hidden;
    }

    public void setQuestions(List<QuizQuestionCreate> qs) {
        this.questions.clear();
        if (qs != null) {
            this.questions.addAll(qs);
        }
    }
}
