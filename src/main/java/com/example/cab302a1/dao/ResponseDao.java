package com.example.cab302a1.dao;

import com.example.cab302a1.DBconnection;
import com.example.cab302a1.model.QuestionResponse;
import com.mysql.cj.protocol.Resultset;
import org.flywaydb.core.internal.jdbc.Results;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class ResponseDao {
    public void saveResponse(int _attemptId, List<QuestionResponse> _response){
        String sql = "INSERT INTO question_responses (attempt_id, question_id, option_id, is_correct) VALUES (?, ?, ?,?)";

        try(Connection conn = DBconnection.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql)){

            for(QuestionResponse r : _response){
                pstmt.setInt(1, _attemptId);
                pstmt.setInt(2, r.getQuestion_id());
                pstmt.setInt(3, r.getOption_id());
                pstmt.setBoolean(4, r.getIs_Correct());

                pstmt.addBatch(); // Batch for performance
            }

            pstmt.executeBatch();

        } catch(SQLException e){
            e.printStackTrace();
        }
    }

    public int calculateScoreFromResponses(int _attemptId){
        String sql = "SELECT COUNT(response_id) AS score FROM question_responses WHERE is_correct = True AND attempt_id = ?";

        try(Connection conn = DBconnection.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql)){

            pstmt.setInt(1, _attemptId);
            try(ResultSet rs = pstmt.executeQuery()){
                if(rs.next()){
                    return rs.getInt("score");
                }
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return -1;
    }
}
