package com.example.cab302a1.dao.jdbi;

import com.example.cab302a1.DBconnection;
import com.example.cab302a1.dao.QuestionDao;
import com.example.cab302a1.dao.ReviewDao;
import com.example.cab302a1.model.Quiz;
import com.example.cab302a1.model.QuizReview;
import org.jdbi.v3.core.Jdbi;

import java.util.List;

/**
 * {@code JdbiReviewDao} provides a JDBI-based implementation of the {@link ReviewDao} interface.
 * <p>
 * This class retrieves quiz attempt data, including titles, scores, and feedback,
 * for a given user by joining {@code quiz_attempts} and {@code quizzes}.
 * It also uses {@link QuestionDao} to determine the total number of questions per quiz.
 * </p>
 */
public class JdbiReviewDao implements ReviewDao {

    private final Jdbi jdbi;
    private final QuestionDao questionDao;

    /**
     * Constructs a JdbiReviewDao with required dependencies.
     *
     * @param questionDao The QuestionDao used for retrieving quiz question counts.
     */
    public JdbiReviewDao(QuestionDao questionDao) {
        this.jdbi = DBconnection.getJdbi();
        this.questionDao = questionDao;
    }

    /**
     * {@inheritDoc}
     * <p>
     * Retrieves all quiz attempts by a given user, joining quiz metadata and feedback.
     * </p>
     */
    @Override
    public List<QuizReview> getAllAttemptsById(int userId) {
        String sql = """
            SELECT qa.attempt_id,
                   qa.quiz_id,
                   qa.answered_by,
                   q.title,
                   qa.score,
                   qa.feedback
            FROM quiz_attempts qa
            JOIN quizzes q ON qa.quiz_id = q.quiz_id
            WHERE qa.answered_by = :userId AND qa.is_completed = TRUE
        """;

        return jdbi.withHandle(handle ->
                handle.createQuery(sql)
                        .bind("userId", userId)
                        .map((rs, ctx) -> {
                            int attemptId = rs.getInt("attempt_id");
                            int quizId = rs.getInt("quiz_id");
                            int score = rs.getInt("score");
                            String feedback = rs.getString("feedback");
                            String title = rs.getString("title");

//                            // Retrieve total number of questions using QuestionDao
                            int totalQuestions = questionDao.getNumQuestion(quizId);


                            return new QuizReview(
                                    attemptId,
                                    quizId,
                                    userId,
                                    title,
                                    score,
                                    totalQuestions,
                                    feedback
                            );
                        })
                        .list()
        );
    }

    public List<QuizReview> getStudentAttemptsforTeacher(int userId, int teacherId){
        String sql = """
                 SELECT qa.attempt_id,
                               qa.quiz_id,
                               qa.answered_by,
                               q.title,
                               qa.score,
                               qa.feedback
                        FROM quiz_attempts qa
                        JOIN quizzes q ON qa.quiz_id = q.quiz_id
                        WHERE qa.answered_by = :userId
                          AND qa.is_completed = TRUE
                          AND q.created_by = :teacherId
                """;

        return jdbi.withHandle(handle ->
                handle.createQuery(sql)
                        .bind("userId", userId)
                        .bind("teacherId", teacherId)
                        .map((rs, ctx) -> {
                            int attemptId = rs.getInt("attempt_id");
                            int quizId = rs.getInt("quiz_id");
                            String title = rs.getString("title");
                            int score = rs.getInt("score");
                            String feedback = rs.getString("feedback");

                            int totalQuestions = questionDao.getNumQuestion(quizId);

                            return new QuizReview(
                                    attemptId,
                                    quizId,
                                    userId,
                                    title,
                                    score,
                                    totalQuestions,
                                    feedback
                            );
                        })
                        .list()

        );
    }
}
