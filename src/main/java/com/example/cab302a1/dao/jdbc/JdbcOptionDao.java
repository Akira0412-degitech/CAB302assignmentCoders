//package com.example.cab302a1.dao.jdbc;
//
//import com.example.cab302a1.DBconnection;
//import com.example.cab302a1.dao.OptionDao;
//import com.example.cab302a1.model.QuizChoiceCreate;
//
//import java.sql.Connection;
//import java.sql.PreparedStatement;
//import java.sql.ResultSet;
//import java.sql.SQLException;
//import java.util.ArrayList;
//import java.util.List;
//
//
///**
// * {@code JdbcOptionDao} provides a JDBC-based implementation of the {@link OptionDao} interface.
// * <p>
// * This class manages CRUD operations for quiz question options in the database.
// * It performs SQL operations on the {@code question_options} table using
// * the {@link DBconnection} utility for database connectivity.
// * </p>
// * <p>
// * Each method uses a try-with-resources block to ensure proper resource management,
// * automatically closing JDBC connections, statements, and result sets.
// * </p>
// */
//public class JdbcOptionDao implements OptionDao {
//
//    /**
//     * {@inheritDoc}
//     * <p>
//     * This implementation executes an {@code INSERT} statement on the {@code question_options} table
//     * and retrieves the generated {@code option_id}.
//     * Returns {@code -1} if the insertion fails or no key is generated.
//     * </p>
//     */
//    @Override
//    public int insertOption(QuizChoiceCreate choice) {
//        String sql = "INSERT INTO question_options (question_id, option_text, is_correct) VALUES (?, ?, ?)";
//        try (Connection conn = DBconnection.getConnection();
//             PreparedStatement pstmt = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
//
//            pstmt.setInt(1, choice.getQuestion_id());
//            pstmt.setString(2, choice.getText());
//            pstmt.setBoolean(3, choice.isCorrect());
//
//            pstmt.executeUpdate();
//
//            try (ResultSet rs = pstmt.getGeneratedKeys()) {
//                if (rs.next()) {
//                    return rs.getInt(1);
//                }
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//        return -1; //Return -1 when failing
//    }
//
//    /**
//     * {@inheritDoc}
//     * <p>
//     * This implementation retrieves all option records associated with a specific question
//     * by executing a {@code SELECT} query on the {@code question_options} table.
//     * </p>
//     */
//    @Override
//    public List<QuizChoiceCreate> getOptionsByQuestionId(int questionId) {
//        List<QuizChoiceCreate> options = new ArrayList<>();
//        String sql = "SELECT option_id, question_id, option_text, is_correct FROM question_options WHERE question_id = ?";
//
//        try (Connection conn = DBconnection.getConnection();
//             PreparedStatement pstmt = conn.prepareStatement(sql)) {
//
//            pstmt.setInt(1, questionId);
//
//            try (ResultSet rs = pstmt.executeQuery()) {
//                while (rs.next()) {
//                    QuizChoiceCreate choice = new QuizChoiceCreate();
//                    choice.setOption_id(rs.getInt("option_id"));
//                    choice.setQuestion_id(rs.getInt("question_id"));
//                    choice.setText(rs.getString("option_text"));
//                    choice.setIs_correct(rs.getBoolean("is_correct"));
//                    options.add(choice);
//                }
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//
//        return options;
//    }
//
//    /**
//     * {@inheritDoc}
//     * <p>
//     * Executes an {@code UPDATE} statement on the {@code question_options} table
//     * to modify the text and correctness flag of an existing option record.
//     * </p>
//     */
//    @Override
//    public void updateOption(QuizChoiceCreate choice) {
//        String sql = "UPDATE question_options SET option_text = ?, is_correct = ? WHERE option_id = ?";
//        try (Connection conn = DBconnection.getConnection();
//             PreparedStatement pstmt = conn.prepareStatement(sql)) {
//
//            pstmt.setString(1, choice.getText());
//            pstmt.setBoolean(2, choice.isCorrect());
//            pstmt.setInt(3, choice.getOption_id());
//
//            pstmt.executeUpdate();
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//    }
//
//
//}
