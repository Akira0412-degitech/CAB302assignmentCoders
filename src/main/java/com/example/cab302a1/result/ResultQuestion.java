package com.example.cab302a1.result;

import com.example.cab302a1.model.QuizQuestionCreate;

public class ResultQuestion {
    private QuizQuestionCreate question;
    private int chosenOption_id;

    public ResultQuestion(QuizQuestionCreate _question, int _chosenOption_id){
        this.question = _question;
        this.chosenOption_id = _chosenOption_id;
    }

}
