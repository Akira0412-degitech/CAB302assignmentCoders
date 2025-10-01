package com.example.cab302a1.result;

import com.example.cab302a1.model.QuizQuestionCreate;

public class ResultQuestion {
    private QuizQuestionCreate question;
    private int chosenOption_id;

    public ResultQuestion(QuizQuestionCreate _question, int _chosenOption_id){
        this.question = _question;
        this.chosenOption_id = _chosenOption_id;
    }

   public QuizQuestionCreate getQuestion(){
        return question;
   }

   public void setQuestion(QuizQuestionCreate question){
        this.question = question;
   }

   public int getChosenOption_id(){
        return chosenOption_id;
   }
   public void setChosenOption_id(int _chosenOption_id){
        this.chosenOption_id = _chosenOption_id;
   }

}
