package com.example.cab302a1.dao.jdbc;

import com.example.cab302a1.DBconnection;
import com.example.cab302a1.dao.QuizDao;
import com.example.cab302a1.model.Quiz;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
/**
 * {@code JdbcQuizDao} provides a JDBC-based implementation of the {@link QuizDao} interface.
 * <p>
 * This class handles CRUD operations for quizzes stored in the {@code quizzes} table,
 * including creation, retrieval, updates, and visibility changes.
 * </p>
 * <p>
 * All database connections are obtained via the {@link DBconnection} utility class,
 * and resources are automatically closed using try-with-resources statements.
 * </p>
 */

public class JdbcQuizDao implements QuizDao {

    /**
     * {@inheritDoc}
     * <p>
     * Executes a {@code SELECT} query on the {@code quizzes} table
     * to retrieve all quiz records, including their titles, descriptions,
     * creators, and visibility status.
     * </p>
     */
    @Override
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

    /**
     * {@inheritDoc}
     * <p>
     * Executes an {@code INSERT} statement to add a new quiz record into the
     * {@code quizzes} table. The {@code is_Hidden} flag defaults to {@code false}.
     * Returns the generated quiz ID or {@code -1} if insertion fails.
     * </p>
     */
    @Override
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
    /**
     * {@inheritDoc}
     * <p>
     * Executes an {@code UPDATE} statement to modify the title and description
     * of an existing quiz record identified by its quiz ID.
     * </p>
     */
    @Override
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

    /**
     * {@inheritDoc}
     * <p>
     * Executes a {@code SELECT} query to find a quiz by its unique ID.
     * Returns a {@link Quiz} object if found, or {@code null} if no record exists.
     * </p>
     */
    @Override
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

    /**
     * {@inheritDoc}
     * <p>
     * Updates the {@code is_Hidden} flag of a quiz to control its visibility
     * within the application.
     * </p>
     */
    @Override
    public void updateQuizStatus(int _quizId, boolean _IsHidden){
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
