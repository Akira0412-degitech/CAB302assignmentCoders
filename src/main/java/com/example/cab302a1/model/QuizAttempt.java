package com.example.cab302a1.model;

import java.sql.Timestamp;

public class QuizAttempt {
    private int attempt_id;
    private int quiz_id;
    private int answered_by;
    private Timestamp start_time;
    private Timestamp end_time;
    private int score;
    private boolean is_Completed;

    public void setQuiz_id(int _quiz_id){
        quiz_id = _quiz_id;
    }
    public void setAnswered_by(int _answered_by){
        answered_by = _answered_by;
    }
    public void setStart_time(Timestamp _start_time){
        start_time = _start_time;
    }
    public void setEnd_time(Timestamp _end_time){
        end_time = _end_time;
    }
    public void setScore(int _score){
        score = _score;
    }
    public void setIs_Completed(Boolean _is_Completed){
        is_Completed = _is_Completed;
    }

    public int getAttempt_id(){ return attempt_id;}
    public int getQuiz_id(){return quiz_id;}
    public int getAnswered_by(){return answered_by;}
    public Timestamp getStart_time(){return start_time;}
    public Timestamp getEnd_time(){return end_time;}
    public int getScore(){return score;}
    public boolean getIs_Completed(){return is_Completed;}
}
