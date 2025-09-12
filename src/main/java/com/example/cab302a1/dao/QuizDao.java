package com.example.cab302a1.dao;

import com.example.cab302a1.DBconnection;
import com.example.cab302a1.model.Quiz;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class QuizDao {
    public List<Quiz> getAllQuizzes(){
        List<Quiz> quizzes = new ArrayList<>();

        String sql = "SELECT quiz_id, title, description, created_by FROM quizzes";

        try(Connection conn = DBconnection.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery()){

            while (rs.next()){
                Quiz q = new Quiz();
                q.setQuizId(rs.getInt("quiz_id"));
                q.setTitle(rs.getString("title"));
                q.setDescription(rs.getString("description"));
                q.setCreated_by(rs.getInt("created_by"));
                quizzes.add(q);
            }

        } catch (SQLException e){
            e.printStackTrace();
        }
        return quizzes;
    }

    public int insertQuiz(Quiz _quiz){
        String sql = "INSERT INTO quizzes (title, description, created_by) VALUES (?, ?, ?)";

        try(Connection conn =DBconnection.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)){
            pstmt.setString(1, _quiz.getTitle());
            pstmt.setString(2, _quiz.getDescription());
            pstmt.setInt(3, _quiz.getCreated_by());
            pstmt.executeUpdate();

            try(ResultSet rs = pstmt.getGeneratedKeys()){
                if(rs.next()){
                    return rs.getInt(1);
                }
            }

        } catch (SQLException e){
            e.printStackTrace();
        }
        return -1;
    }
}
