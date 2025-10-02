package com.example.cab302a1.dao;

import com.example.cab302a1.DBconnection;
import com.example.cab302a1.model.Quiz;
import com.example.cab302a1.model.QuizQuestionCreate;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class QuestionDao {

    public List<QuizQuestionCreate> getAllQuestions(int  _quiz_id) {
        List<QuizQuestionCreate> questions = new ArrayList<>();
        String sql = "SELECT question_id, quiz_id, statement, explanation FROM questions WHERE quiz_id = ?";

        try (Connection conn = DBconnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, _quiz_id);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    QuizQuestionCreate q = new QuizQuestionCreate(
                            rs.getInt("question_id"),
                            rs.getInt("quiz_id"),
                            rs.getString("statement"),
                            rs.getString("explanation"));

                    questions.add(q);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return questions;
    }

    public int insertQuestion(QuizQuestionCreate q){
        String sql = "INSERT INTO questions (quiz_id, statement, type, explanation) VALUES (?,?, ?, ?)";

//        For now we have only one type "MCQ"
        String type = "MCQ";
        try(Connection conn = DBconnection.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)){
            pstmt.setInt(1, q.getQuizId());
            pstmt.setString(2, q.getQuestionText());
            pstmt.setString(3, type);
            pstmt.setString(4, q.getExplanation());

            pstmt.executeUpdate();

            try(ResultSet rs = pstmt.getGeneratedKeys()){
                if(rs.next()){
                    return rs.getInt(1);
                }
            }

        }catch (SQLException e){
            e.printStackTrace();
        }
        return -1;
    }

    public void updateQuestion(QuizQuestionCreate q) {
        String sql = "UPDATE questions SET statement = ?, explanation = ? WHERE question_id = ?";
        try (Connection conn = DBconnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, q.getQuestionText());
            pstmt.setString(2, q.getExplanation());
            pstmt.setInt(3, q.getQuestionId());

            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public int getNumQuestion(int _quiz_id){
        String sql = "SELECT COUNT(question_id) cnt FROM questions WHERE quiz_id = ?";

        try(Connection conn = DBconnection.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, _quiz_id);

            try(ResultSet rs = pstmt.executeQuery()){
                if(rs.next()){
                    int count_ques = rs.getInt("cnt");
                    return count_ques;
                }
            }
            
        } catch (SQLException e){
            e.printStackTrace();
        }
        return -1;
    }

    public List<QuizQuestionCreate> getQuestionsByQuizId(int _quiz_id){
        String sql = "SELECT question_id, quiz_id, statement, type, explanation" +
                "FROM questions WHERE quiz_id = ?";
        List<QuizQuestionCreate> questions = new ArrayList<>();

        try(Connection conn = DBconnection.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql)){

            pstmt.setInt(1, _quiz_id);

            try(ResultSet rs = pstmt.executeQuery()){
                if(rs.next()){
                    QuizQuestionCreate q = new QuizQuestionCreate(
                            rs.getInt("question_id"),
                            rs.getInt("quiz_id"),
                            rs.getString("statement"),
                            rs.getString("explanation")
                    );
                    questions.add(q);
                }
            }
        } catch (SQLException e){
            e.printStackTrace();
        }
        return questions;
    }
}
