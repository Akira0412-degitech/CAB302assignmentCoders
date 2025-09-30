package com.example.cab302a1.model;

import java.sql.Timestamp;

public class User {
    protected int user_id;
    protected String username;
    protected String email;
    protected String password;
    protected String role;
    protected Timestamp created_at;

    public User(int _user_id, String _username, String _email, String _role, Timestamp _created_at){
        this.user_id = _user_id;
        this.username = _username;
        this.email = _email;
        this.role = _role;
        this.created_at = _created_at;
    }

    public int getUser_id(){return user_id;}
    public String getUsername(){return username;}
    public String getEmail(){return email;}

    public String getRole(){return role;}
    public Timestamp getCreated_at(){return created_at;}


}
