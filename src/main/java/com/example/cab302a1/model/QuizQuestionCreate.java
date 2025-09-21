package com.example.cab302a1.model;

import java.util.ArrayList;
import java.util.List;


public class QuizQuestionCreate {
    private int question_id;
    private int quiz_id;
    private String questionText;
    private final List<QuizChoiceCreate> choices = new ArrayList<>();
    private String explanation;

    public void setQuiz_id(int _quizId){
        quiz_id = _quizId;
    }
    public void setQuestionText(String questionText) { this.questionText = questionText; }
    public void setQuestion_id(int _questionId) {
        question_id = _questionId;
    }
    public void setExplanation(String _explanation){
        explanation = _explanation;
    }

    public List<QuizChoiceCreate> getChoices() { return choices; }
    public String getQuestionText() { return questionText; }
    public int getQuestionId(){return question_id;}
    public int getQuizId(){return quiz_id;}
    public String  getExplanation(){return explanation;}
}
