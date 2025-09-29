package com.example.cab302a1.model;

import java.sql.Timestamp;

public class Teacher extends User{

    public Teacher(int _user_id, String _username, String _email, String _role, Timestamp _created_at){
        super(_user_id, _username, _email, _role,  _created_at);
    }
}
