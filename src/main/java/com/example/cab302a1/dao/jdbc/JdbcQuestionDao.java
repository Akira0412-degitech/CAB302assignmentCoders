//package com.example.cab302a1.dao.jdbc;
//
//import com.example.cab302a1.DBconnection;
//import com.example.cab302a1.dao.QuestionDao;
//import com.example.cab302a1.model.QuizQuestionCreate;
//
//import java.sql.Connection;
//import java.sql.PreparedStatement;
//import java.sql.ResultSet;
//import java.sql.SQLException;
//import java.util.ArrayList;
//import java.util.List;
//
///**
// * {@code JdbcQuestionDao} provides a JDBC-based implementation of the {@link QuestionDao} interface.
// * <p>
// * It manages CRUD operations for quiz questions by interacting directly with
// * the {@code questions} table through SQL queries. All database connections are
// * obtained using the {@link DBconnection} utility.
// * </p>
// * <p>
// * This implementation assumes all questions are of type {@code "MCQ"} unless otherwise specified.
// * </p>
// */
//
//public class JdbcQuestionDao implements QuestionDao {
//
//    /**
//     * {@inheritDoc}
//     * <p>
//     * Executes a {@code SELECT} query on the {@code questions} table to retrieve
//     * all question records belonging to a specific quiz.
//     * </p>
//     */
//    @Override
//    public List<QuizQuestionCreate> getAllQuestions(int  _quiz_id) {
//        List<QuizQuestionCreate> questions = new ArrayList<>();
//        String sql = "SELECT question_id, quiz_id, statement, explanation FROM questions WHERE quiz_id = ?";
//
//        try (Connection conn = DBconnection.getConnection();
//             PreparedStatement pstmt = conn.prepareStatement(sql)) {
//
//            pstmt.setInt(1, _quiz_id);
//
//            try (ResultSet rs = pstmt.executeQuery()) {
//                while (rs.next()) {
//                    QuizQuestionCreate q = new QuizQuestionCreate(
//                            rs.getInt("question_id"),
//                            rs.getInt("quiz_id"),
//                            rs.getString("statement"),
//                            rs.getString("explanation"));
//
//                    questions.add(q);
//                }
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//
//        return questions;
//    }
//
//    /**
//     * {@inheritDoc}
//     * <p>
//     * Executes an {@code INSERT} statement on the {@code questions} table to add a new
//     * question record. The question type is currently fixed to {@code "MCQ"}.
//     * Returns the generated {@code question_id}, or {@code -1} if the operation fails.
//     * </p>
//     */
//    @Override
//    public int insertQuestion(QuizQuestionCreate q){
//        String sql = "INSERT INTO questions (quiz_id, statement, type, explanation) VALUES (?,?, ?, ?)";
//
////        For now we have only one type "MCQ"
//        String type = "MCQ";
//        try(Connection conn = DBconnection.getConnection();
//            PreparedStatement pstmt = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)){
//            pstmt.setInt(1, q.getQuizId());
//            pstmt.setString(2, q.getQuestionText());
//            pstmt.setString(3, type);
//            pstmt.setString(4, q.getExplanation());
//
//            pstmt.executeUpdate();
//
//            try(ResultSet rs = pstmt.getGeneratedKeys()){
//                if(rs.next()){
//                    return rs.getInt(1);
//                }
//            }
//
//        }catch (SQLException e){
//            e.printStackTrace();
//        }
//        return -1;
//    }
//
//    /**
//     * {@inheritDoc}
//     * <p>
//     * Executes an {@code UPDATE} statement to modify the text and explanation
//     * of an existing question in the {@code questions} table.
//     * </p>
//     */
//    @Override
//    public void updateQuestion(QuizQuestionCreate q) {
//        String sql = "UPDATE questions SET statement = ?, explanation = ? WHERE question_id = ?";
//        try (Connection conn = DBconnection.getConnection();
//             PreparedStatement pstmt = conn.prepareStatement(sql)) {
//
//            pstmt.setString(1, q.getQuestionText());
//            pstmt.setString(2, q.getExplanation());
//            pstmt.setInt(3, q.getQuestionId());
//
//            pstmt.executeUpdate();
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//    }
//
//    /**
//     * {@inheritDoc}
//     * <p>
//     * Executes a {@code COUNT()} query on the {@code questions} table
//     * to determine how many questions belong to a given quiz.
//     * Returns {@code -1} if the query fails.
//     * </p>
//     */
//    @Override
//    public int getNumQuestion(int _quiz_id){
//        String sql = "SELECT COUNT(question_id) cnt FROM questions WHERE quiz_id = ?";
//
//        try(Connection conn = DBconnection.getConnection();
//            PreparedStatement pstmt = conn.prepareStatement(sql)) {
//
//            pstmt.setInt(1, _quiz_id);
//
//            try(ResultSet rs = pstmt.executeQuery()){
//                if(rs.next()){
//                    int count_ques = rs.getInt("cnt");
//                    return count_ques;
//                }
//            }
//
//        } catch (SQLException e){
//            e.printStackTrace();
//        }
//        return -1;
//    }
//
//
//    /**
//     * {@inheritDoc}
//     * <p>
//     * Executes a {@code SELECT} query on the {@code questions} table to retrieve
//     * all question records associated with a given quiz ID. This method is functionally
//     * similar to {@link #getAllQuestions(int)} but may be used for different contexts.
//     * </p>
//     */
//    @Override
//    public List<QuizQuestionCreate> getQuestionsByQuizId(int _quiz_id){
//        String sql = "SELECT question_id, quiz_id, statement, type, explanation " +
//                "FROM questions WHERE quiz_id = ?";
//        List<QuizQuestionCreate> questions = new ArrayList<>();
//
//        try(Connection conn = DBconnection.getConnection();
//            PreparedStatement pstmt = conn.prepareStatement(sql)){
//
//            pstmt.setInt(1, _quiz_id);
//
//            try(ResultSet rs = pstmt.executeQuery()){
//                while(rs.next()){
//                    QuizQuestionCreate q = new QuizQuestionCreate(
//                            rs.getInt("question_id"),
//                            rs.getInt("quiz_id"),
//                            rs.getString("statement"),
//                            rs.getString("explanation")
//                    );
//                    questions.add(q);
//                }
//            }
//        } catch (SQLException e){
//            e.printStackTrace();
//        }
//        return questions;
//    }
//
//}
