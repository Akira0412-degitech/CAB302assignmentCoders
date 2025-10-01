package com.example.cab302a1.result;

import com.example.cab302a1.model.Quiz;

import java.util.List;

public class ResultDetail {
    private Quiz quiz;
    private List<ResultQuestion> questions;

    public ResultDetail(Quiz _quiz, List<ResultQuestion> _resultQuestions){
        this.quiz = _quiz;

        this.questions = _resultQuestions;
    }

    public Quiz getQuiz(){
        return quiz;
    }

    public void setQuiz(Quiz _quiz){
        this.quiz = _quiz;
    }


    public List<ResultQuestion> getResultQuestions(){
        return questions;
    }

    public void setResultQuestion(List<ResultQuestion> _resultQuestions){
        this.questions = _resultQuestions;
    }






}
