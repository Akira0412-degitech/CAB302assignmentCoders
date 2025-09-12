package com.example.cab302a1.model;

public class QuizChoiceCreate {
    private int option_id;
    private int question_id;
    private String text;
    private boolean is_answer;

    public QuizChoiceCreate() {
    }
    public QuizChoiceCreate(String text, boolean _is_answer) {
        this.text = text; // put the value recieved from cstor into the variable called
        is_answer = _is_answer;
    }
    public void setOption_id(int _optionId){
        option_id = _optionId;
    }
    public void setQuestion_id(int _question_id){
        question_id = _question_id;
    }
    public void setText(String text) { this.text = text; } //change text to settext


    public int getQuestion_id(){return question_id;}
    public String getText() { return text; } //extract selected text
    public boolean isAnswer() { return is_answer; } // T/F check
    public void setIs_answer(boolean is_answer) { this.is_answer = is_answer; }
}

