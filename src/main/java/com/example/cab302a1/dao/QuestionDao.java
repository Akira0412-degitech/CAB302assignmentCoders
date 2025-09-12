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

    public List<QuizQuestionCreate> getAllQuestions(Quiz _quiz) {
        List<QuizQuestionCreate> questions = new ArrayList<>();
        String sql = "SELECT question_id, quiz_id, statement, explanation FROM questions WHERE quiz_id = ?";

        try (Connection conn = DBconnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, _quiz.getQuizId());

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    QuizQuestionCreate q = new QuizQuestionCreate();
                    q.setQuestion_id(rs.getInt("question_id"));
                    q.setQuiz_id(rs.getInt("quiz_id"));
                    q.setQuestionText(rs.getString("statement"));
                    q.setExplanation(rs.getString("explanation"));
                    questions.add(q);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return questions;
    }

    public void insertQuestion(QuizQuestionCreate q){
        String sql = "INSERT INTO questions (statement, type, explanation) VALUES (?, ?, ?)";

//        For now we have only one type "MCQ"
        String type = "MCQ";
        try(Connection conn = DBconnection.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql)){
            pstmt.setString(1, q.getQuestionText());
            pstmt.setString(2, type);
            pstmt.setString(3, q.getExplanation());

            pstmt.executeUpdate();

        }catch (SQLException e){
            e.printStackTrace();
        }
    }
}
