//package com.example.cab302a1.dao.jdbc;
//
//import com.example.cab302a1.DBconnection;
//import com.example.cab302a1.dao.QuestionDao;
//import com.example.cab302a1.dao.ResponseDao;
//import com.example.cab302a1.dao.ReviewDao;
//import com.example.cab302a1.model.QuizReview;
//
//import java.sql.Connection;
//import java.sql.PreparedStatement;
//import java.sql.ResultSet;
//import java.sql.SQLException;
//import java.util.ArrayList;
//import java.util.List;
//
///**
// * {@code JdbcReviewDao} provides a JDBC-based implementation of the {@link ReviewDao} interface.
// * <p>
// * This class retrieves quiz attempt data, including quiz titles, scores, and feedback,
// * for a specific user by joining the {@code quiz_attempts} and {@code quizzes} tables
// * on their shared {@code quiz_id} column.
// * </p>
// * <p>
// * It also leverages {@link QuestionDao} to compute the total number of questions
// * in each quiz, ensuring that reviews include both raw score and quiz context.
// * </p>
// */
//
//public class JdbcReviewDao implements ReviewDao {
//
//
//    private final QuestionDao questionDao;
//
//    /**
//     * Constructs a JdbcReviewDao with the required dependencies.
//     *
//     * @param questionDao The QuestionDao used for retrieving quiz question information.
//     */
//    public JdbcReviewDao(QuestionDao questionDao) {
//
//        this.questionDao = questionDao;
//    }
//
//    /**
//     * {@inheritDoc}
//     *
//     * <p>This method executes a SQL query that joins {@code quiz_attempts} and {@code quizzes}
//     * to retrieve all attempts for the given user. It calculates total questions for
//     * each quiz using the injected {@link QuestionDao}.</p>
//     *
//     * @param userId The unique ID of the user whose quiz attempts should be retrieved.
//     * @return A list of {@link QuizReview} objects containing quiz metadata,
//     *         scores, and feedback. Returns an empty list if no records are found.
//     */
//    @Override
//    public List<QuizReview> getAllAttemptsById(int userId) {
//        List<QuizReview> reviews = new ArrayList<>();
//
//        String sql = """
//            SELECT qa.attempt_id, qa.quiz_id, qa.answered_by, q.title, qa.score, qa.feedback
//            FROM quiz_attempts qa
//            JOIN quizzes q ON qa.quiz_id = q.quiz_id
//            WHERE qa.answered_by = ?
//            """;
//
//        try (Connection conn = DBconnection.getConnection();
//             PreparedStatement pstmt = conn.prepareStatement(sql)) {
//
//            pstmt.setInt(1, userId);
//
//            try (ResultSet rs = pstmt.executeQuery()) {
//                while (rs.next()) {
//                    int attemptId = rs.getInt("attempt_id");
//                    int quizId = rs.getInt("quiz_id");
//                    int score = rs.getInt("score");
//                    String feedback = rs.getString("feedback");
//                    String title = rs.getString("title");
//
//                    // Retrieve total number of questions in the quiz
//                    int totalQuestions = questionDao.getNumQuestion(quizId);
//
//                    QuizReview review = new QuizReview(
//                            attemptId,
//                            quizId,
//                            userId,
//                            title,
//                            score,
//                            totalQuestions,
//                            feedback
//                    );
//
//                    reviews.add(review);
//                }
//            }
//
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//
//        return reviews;
//    }
//
//    @Override
//    public List<QuizReview> getStudentAttemptsforTeacher(int userId, int teacherId) {
//        return List.of();
//    }
//}
