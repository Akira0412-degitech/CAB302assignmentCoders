package com.example.cab302a1.model;

public class QuestionResponse {
    private int response_id;
    private int attempt_id;
    private int question_id;
    private int option_id;
    private boolean is_correct;

    public void setResponse_id(int _response_id) {
        this.response_id = _response_id;
    }
    public void setAttempt_id(int _attempt_id){
        attempt_id = _attempt_id;
    }
    public void setQuestion_id(int _question_id){
        question_id = _question_id;
    }
    public void setOption_id(int _option_id){
        option_id = _option_id;
    }
    public void setIs_correct(boolean _is_correct){
        is_correct = _is_correct;
    }
    public int getResponse_id(){return response_id;}
    public int getAttempt_id(){return attempt_id;}
    public int getQuestion_id(){return question_id;}
    public int getOption_id(){return option_id;}
    public boolean getIs_Correct(){return is_correct;}
}
