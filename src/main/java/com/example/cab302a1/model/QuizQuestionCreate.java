package com.example.cab302a1.model;

import java.util.ArrayList;
import java.util.List;


public class QuizQuestionCreate {
    private int question_id;
    private int quiz_id;
    private String questionText;
    private List<QuizChoiceCreate> choices = new ArrayList<>();
    private String explanation;

    public QuizQuestionCreate(){

    }

    public QuizQuestionCreate(int _question_id, int _quiz_id, String _questionText, String _explanation){
        this.question_id = _question_id;
        this.quiz_id = _quiz_id;
        this.questionText = _questionText;
        this.explanation = _explanation;
    }

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
    public void setOptions(List<QuizChoiceCreate> _choices){
        this.choices = _choices;
    }

    public List<QuizChoiceCreate> getChoices() { return choices; }
    public String getQuestionText() { return questionText; }
    public int getQuestionId(){return question_id;}
    public int getQuizId(){return quiz_id;}
    public String  getExplanation(){return explanation;}
}
