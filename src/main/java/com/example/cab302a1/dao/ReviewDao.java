package com.example.cab302a1.dao;

import com.example.cab302a1.DBconnection;
import com.example.cab302a1.model.QuizReview;
import com.example.cab302a1.model.Student;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ReviewDao {
    public List<QuizReview> getAllAttemptsById(int _user_id){
        List<QuizReview> reviews =  new ArrayList<>();
        ResponseDao responseDao = new ResponseDao();
        String sql = "SELECT q.title, qa.score, qa.feedback, qa.attempt_id" +
                        "FROM quiz_attempts qa " +
                        "JOIN quizzes q ON qa.quiz_id = q.quiz_id " +
                        "WHERE qa.answered_by = ?";
        try(Connection conn = DBconnection.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql)){
            pstmt.setInt(1, _user_id);

            try(ResultSet rs = pstmt.executeQuery()){
                while(rs.next()){
                    int attemptId = rs.getInt("attempt_id");
                    int QuestionNum = responseDao.calculateScoreFromResponses(attemptId);

                    QuizReview review = new QuizReview(
                            rs.getString("title"),
                            rs.getInt("score"),
                            QuestionNum,
                            rs.getString("feedback")
                    );
                    reviews.add(review);
                }
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return reviews;
    }

}
