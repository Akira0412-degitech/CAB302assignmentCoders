package com.example.cab302a1.dao;

import com.example.cab302a1.DBconnection;
import com.example.cab302a1.model.Quiz;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class QuizDao {


    public List<Quiz> getAllQuizzes(){
        List<Quiz> quizzes = new ArrayList<>();

        String sql = "SELECT quiz_id, title, description, created_by, is_Hidden FROM quizzes";

        try(Connection conn = DBconnection.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery()){

            while (rs.next()){
                Quiz q = new Quiz(rs.getInt("quiz_id"),
                        rs.getString("title"),
                        rs.getString("description"),
                        rs.getInt("created_by"),
                        rs.getBoolean("is_Hidden"));
                quizzes.add(q);
            }

        } catch (SQLException e){
            e.printStackTrace();
        }
        return quizzes;
    }

    public int insertQuiz(Quiz _quiz){
        String sql = "INSERT INTO quizzes (title, description, created_by, is_Hidden) VALUES (?, ?, ?, False)";

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

    public void updateQuiz(Quiz _quiz) {
        String sql = "UPDATE quizzes SET title = ?, description = ? WHERE quiz_id = ?";
        try (Connection conn = DBconnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, _quiz.getTitle());
            pstmt.setString(2, _quiz.getDescription());
            pstmt.setInt(3, _quiz.getQuizId());

            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public Quiz getQuizById(int _quiz_id){
        String sql = "SELECT * FROM quizzes WHERE quiz_id = ?";

        try(Connection conn = DBconnection.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql)){

            pstmt.setInt(1, _quiz_id);

            try(ResultSet rs = pstmt.executeQuery()){
                if(rs.next()){
                    return new Quiz(
                            rs.getInt("quiz_id"),
                            rs.getString("title"),
                            rs.getString("description"),
                            rs.getInt("created_by"),
                            rs.getBoolean("is_Hidden")
                    );

                }
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return null;
    }

    public void UpdateQuizHidden(int _quizId, boolean _IsHidden){
        String sql = "UPDATE quizzes SET is_Hidden = ? WHERE quiz_id = ?";

        try(Connection conn = DBconnection.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql)){
            {
                pstmt.setBoolean(1, _IsHidden);
                pstmt.setInt(2, _quizId);
                pstmt.executeUpdate();
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
    }
}
