package com.example.cab302a1.dao.jdbc;

import com.example.cab302a1.DBconnection;
import com.example.cab302a1.dao.AttemptDao;
import com.example.cab302a1.dao.ResponseDao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class JdbcAttemptDao implements AttemptDao {

    @Override
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

    @Override
    public boolean attemptExist(int _attemptId){
        String sql = "SELECT 1 FROM quiz_attempts WHERE attempt_id = ? LIMIT 1";

        try(Connection conn = DBconnection.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql)){

            pstmt.setInt(1, _attemptId);
            try(ResultSet rs = pstmt.executeQuery()){
                if(rs.next()){
                    return true;
                }
            }
        } catch (SQLException e){
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public void endAttempt(int _attempt_id){
        ResponseDao responseDao = new ResponseDao();
        boolean existing = attemptExist(_attempt_id);

        if(!existing){
            System.out.println("Attempt record does not exist");
            return;
        }

        int score = responseDao.calculateScoreFromResponses(_attempt_id);

        String sql = "UPDATE quiz_attempts SET score = ?, is_completed = True WHERE attempt_id = ?";

        try(Connection conn = DBconnection.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql)){

            pstmt.setInt(1, score);
            pstmt.setInt(2, _attempt_id);
            pstmt.executeUpdate();

        } catch (SQLException e){
            e.printStackTrace();
        }
    }


    @Override
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

    @Override
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


    @Override
    public void updateFeedback(int _attemptId, String _feedback){
        String sql = "UPDATE quiz_attempts SET feedback = ? WHERE attempt_id = ?";

        try(Connection conn = DBconnection.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql)){

            pstmt.setString(1, _feedback);
            pstmt.setInt(2, _attemptId);

            pstmt.executeUpdate();

        } catch (SQLException e){
            e.printStackTrace();
        }
    }

    @Override
    public Integer getAttemptId(int _quiz_id, int _user_id){
        String sql = "SELECT attempt_id FROM quiz_attempts WHERE is_completed = true AND quiz_id = ? AND answered_by = ? ORDER BY attempt_id DESC LIMIT 1";

        try(Connection conn = DBconnection.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql)){
            pstmt.setInt(1, _quiz_id);
            pstmt.setInt(2, _user_id);

            try(ResultSet rs = pstmt.executeQuery()){
                if(rs.next()){
                    return rs.getInt("attempt_id");
                }
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return null;
    }

}
