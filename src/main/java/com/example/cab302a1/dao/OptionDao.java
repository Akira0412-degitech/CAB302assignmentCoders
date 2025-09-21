package com.example.cab302a1.dao;

import com.example.cab302a1.DBconnection;
import com.example.cab302a1.model.QuizChoiceCreate;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class OptionDao {

    // 1. Insert option and return generated option_id
    public int insertOption(QuizChoiceCreate choice) {
        String sql = "INSERT INTO question_options (question_id, option_text, is_correct) VALUES (?, ?, ?)";
        try (Connection conn = DBconnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {

            pstmt.setInt(1, choice.getQuestion_id());
            pstmt.setString(2, choice.getText());
            pstmt.setBoolean(3, choice.isCorrect());

            pstmt.executeUpdate();

            try (ResultSet rs = pstmt.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1; //Return -1 when failing
    }

    // 2. Get all options by question_id
    public List<QuizChoiceCreate> getOptionsByQuestionId(int questionId) {
        List<QuizChoiceCreate> options = new ArrayList<>();
        String sql = "SELECT option_id, question_id, option_text, is_correct FROM question_options WHERE question_id = ?";

        try (Connection conn = DBconnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, questionId);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    QuizChoiceCreate choice = new QuizChoiceCreate();
                    choice.setOption_id(rs.getInt("option_id"));
                    choice.setQuestion_id(rs.getInt("question_id"));
                    choice.setText(rs.getString("option_text"));
                    choice.setIs_correct(rs.getBoolean("is_correct"));
                    options.add(choice);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return options;
    }

    public void updateOption(QuizChoiceCreate choice) {
        String sql = "UPDATE question_options SET option_text = ?, is_correct = ? WHERE option_id = ?";
        try (Connection conn = DBconnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, choice.getText());
            pstmt.setBoolean(2, choice.isCorrect());
            pstmt.setInt(3, choice.getOption_id());

            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


}
