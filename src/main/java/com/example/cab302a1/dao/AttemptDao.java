package com.example.cab302a1.dao;

import com.example.cab302a1.DBconnection;
import com.example.cab302a1.model.Quiz;

import java.sql.*;

public class AttemptDao {

    public int startAttempt(int _quiz_id, int _user_id){
        String sql = "INSERT INTO quiz_attempts (quiz_id, answered_by, score, is_completed) VALUES(?, ?, 0, 0)";

        try(Connection conn = DBconnection.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)){
            pstmt.setInt(1, _quiz_id);
            pstmt.setInt(2, _user_id);
            pstmt.executeUpdate();

            try(ResultSet rs = pstmt.getGeneratedKeys()){
                if(rs.next()){
                    return rs.getInt(1);
                }
            }

        } catch(SQLException e){
            e.printStackTrace();
        }
        return -1;
    }

    public Integer getScore(int _quiz_id, int _user_id){
        String sql =  "SELECT score from quiz_attempts WHERE is_completed = true AND quiz_id = ? AND answered_by = ? ORDER BY attempt_id DESC LIMIT 1";

        try(Connection conn = DBconnection.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql)){
            pstmt.setInt(1, _quiz_id);
            pstmt.setInt(2, _user_id);

            try(ResultSet rs = pstmt.executeQuery()){
                if(rs.next()){
                    return rs.getInt("score");
                }
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return null;
    }

    public boolean hasCompleted(int _quiz_id, int _user_id){
        String sql = "SELECT COUNT(attempt_id) AS cnt FROM quiz_attempts WHERE quiz_id = ? AND answered_by = ? AND is_completed = true";

        try(Connection conn = DBconnection.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql)){

            pstmt.setInt(1, _quiz_id);
            pstmt.setInt(2, _user_id);

            try(ResultSet rs = pstmt.executeQuery()){
                if(rs.next()){
                    int count = rs.getInt("cnt");
                    return count > 0;
                }
            }


        } catch (SQLException e){
            e.printStackTrace();
        }
        return false;
    }

}
