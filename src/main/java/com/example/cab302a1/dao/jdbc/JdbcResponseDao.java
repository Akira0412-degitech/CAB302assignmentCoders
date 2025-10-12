package com.example.cab302a1.dao.jdbc;

import com.example.cab302a1.DBconnection;
import com.example.cab302a1.dao.ResponseDao;
import com.example.cab302a1.model.QuestionResponse;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * {@code JdbcResponseDao} provides a JDBC-based implementation of the {@link ResponseDao} interface.
 * <p>
 * This class manages persistence of quiz attempt responses, including saving
 * chosen options, calculating scores, and retrieving selected options for each question.
 * </p>
 * <p>
 * All operations are performed on the {@code question_responses} table using
 * SQL queries executed through the {@link DBconnection} utility.
 * </p>
 */

public class JdbcResponseDao implements ResponseDao {

    /**
     * {@inheritDoc}
     * <p>
     * This implementation performs a batch {@code INSERT} operation into the
     * {@code question_responses} table for improved performance.
     * Each response includes the attempt ID, question ID, chosen option ID,
     * and correctness flag.
     * </p>
     */
    @Override
    public void saveResponse(int _attemptId, List<QuestionResponse> _response){
        if (_response == null || _response.isEmpty()) {
            return; // 👈 Skip DB access if nothing to save
        }
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

    /**
     * {@inheritDoc}
     * <p>
     * Executes a {@code SELECT COUNT()} query to determine how many responses
     * were marked as correct for a given quiz attempt.
     * Returns the total score or {@code -1} if an error occurs.
     * </p>
     */
    @Override
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

    /**
     * {@inheritDoc}
     * <p>
     * Executes a {@code SELECT} query to retrieve the chosen option ID
     * for a specific question within a quiz attempt.
     * Returns {@code -1} if no record is found.
     * </p>
     */
    @Override
    public int getChosenOptionId(int _attempt_id, int _question_id){
        String sql = "SELECT option_id FROM question_responses WHERE attempt_id = ? AND question_id = ?";

        try(Connection conn = DBconnection.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql)){

            pstmt.setInt(1, _attempt_id);
            pstmt.setInt(2, _question_id);

            try(ResultSet rs = pstmt.executeQuery()){
                if(rs.next()){
                    return rs.getInt("option_id");
                }
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return -1;
    }

}
