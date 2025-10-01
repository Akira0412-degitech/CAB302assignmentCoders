package com.example.cab302a1.result;

import com.example.cab302a1.model.Quiz;

import java.util.List;

public class ResultDetail {
    private Quiz quiz;
    private int attempt_id;
    private List<ResultQuestion> questions;

    public ResultDetail(Quiz _quiz, int _attempt_id, List<ResultQuestion> _resultQuestions){
        this.quiz = _quiz;
        this.attempt_id = _attempt_id;
        this.questions = _resultQuestions;
    }

    public Quiz getQuiz(){
        return quiz;
    }

    public void setQuiz(Quiz _quiz){
        this.quiz = _quiz;
    }

    public int getAttempt_id(){
        return this.attempt_id;
    }
    public void setAttempt_id(int _attempt_id){
        this.attempt_id = _attempt_id;
    }

    public List<ResultQuestion> getResultQuestions(){
        return questions;
    }

    public void setResultQuestion(List<ResultQuestion> _resultQuestions){
        this.questions = _resultQuestions;
    }






}
