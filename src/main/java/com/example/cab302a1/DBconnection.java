package com.example.cab302a1;

import java.sql.*;

public class DBconnection {
    private static final String URL =
            "jdbc:mysql://localhost:3306/cab302_quizApp?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
    private static final String USER = "appuser";
    private static final String PASSWORD = "apppass";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
