package com.example.cab302a1.model;

import java.sql.Timestamp;

public class Student extends User{

    public Student(int _user_id, String _email, String _password, String _role, Timestamp _created_at){
       super(_user_id, _email, _password, _role, _created_at);
    }
}
