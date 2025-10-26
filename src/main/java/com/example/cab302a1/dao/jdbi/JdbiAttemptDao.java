package com.example.cab302a1.dao.jdbi;

import com.example.cab302a1.DBconnection;
import com.example.cab302a1.dao.AttemptDao;
import com.example.cab302a1.dao.ResponseDao;
import org.jdbi.v3.core.Jdbi;

/**
 * {@code JdbcAttemptDao} now uses JDBI to simplify database operations.
 * <p>
 * This version removes manual JDBC boilerplate code such as Connection,
 * PreparedStatement, and ResultSet handling, while keeping SQL control.
 * </p>
 * <p>
 * It depends on {@link ResponseDao} for score calculation and uses
 * {@link DBconnection#getJdbi()} to interact with the MySQL database.
 * </p>
 */
public class JdbiAttemptDao implements AttemptDao {

    private final ResponseDao responseDao;
    private final Jdbi jdbi;

    public JdbiAttemptDao(ResponseDao responseDao) {
        this.responseDao = responseDao;
        this.jdbi = DBconnection.getJdbi();
    }

    /**
     * {@inheritDoc}
     * <p>
     * Inserts a new quiz attempt using JDBI and returns the generated attempt ID.
     * </p>
     */
    @Override
    public int startAttempt(int quizId, int userId) {
        return jdbi.withHandle(handle ->
                handle.createUpdate("INSERT INTO quiz_attempts (quiz_id, answered_by, score, is_completed) " +
                                "VALUES (:quizId, :userId, 0, false)")
                        .bind("quizId", quizId)
                        .bind("userId", userId)
                        .executeAndReturnGeneratedKeys("attempt_id")
                        .mapTo(Integer.class)
                        .findOne()
                        .orElse(-1)
        );
    }

    /**
     * {@inheritDoc}
     * <p>
     * Checks whether an attempt record exists.
     * </p>
     */
    @Override
    public boolean attemptExist(int attemptId) {
        return jdbi.withHandle(handle ->
                handle.createQuery("SELECT 1 FROM quiz_attempts WHERE attempt_id = :id LIMIT 1")
                        .bind("id", attemptId)
                        .mapTo(Integer.class)
                        .findOne()
                        .isPresent()
        );
    }

    /**
     * {@inheritDoc}
     * <p>
     * Marks a quiz attempt as completed and updates the calculated score.
     * </p>
     */
    @Override
    public void endAttempt(int attemptId) {
        if (!attemptExist(attemptId)) {
            return;
        }

        int score = responseDao.calculateScoreFromResponses(attemptId);

        jdbi.useHandle(handle ->
                handle.createUpdate("UPDATE quiz_attempts SET score = :score, is_completed = true WHERE attempt_id = :id")
                        .bind("score", score)
                        .bind("id", attemptId)
                        .execute()
        );
    }

    /**
     * {@inheritDoc}
     * <p>
     * Returns the most recent completed score for a user and quiz.
     * </p>
     */
    @Override
    public Integer getScore(int quizId, int userId) {
        return jdbi.withHandle(handle ->
                handle.createQuery("""
                SELECT score FROM quiz_attempts
                WHERE is_completed = true AND quiz_id = :quizId AND answered_by = :userId
                ORDER BY attempt_id DESC LIMIT 1
            """)
                        .bind("quizId", quizId)
                        .bind("userId", userId)
                        .mapTo(Integer.class)
                        .findOne()
                        .orElse(null)
        );
    }

    /**
     * {@inheritDoc}
     * <p>
     * Checks whether a user has completed any attempts for a given quiz.
     * </p>
     */
    @Override
    public boolean hasCompleted(int quizId, int userId) {
        return jdbi.withHandle(handle ->
                handle.createQuery("""
                SELECT COUNT(*) FROM quiz_attempts
                WHERE quiz_id = :quizId AND answered_by = :userId AND is_completed = true
            """)
                        .bind("quizId", quizId)
                        .bind("userId", userId)
                        .mapTo(Integer.class)
                        .findOne()
                        .orElse(0) > 0
        );
    }

    /**
     * {@inheritDoc}
     * <p>
     * Updates teacher feedback for a quiz attempt.
     * </p>
     */
    @Override
    public void updateFeedback(int attemptId, String feedback) {
        jdbi.useHandle(handle ->
                handle.createUpdate("UPDATE quiz_attempts SET feedback = :feedback WHERE attempt_id = :id")
                        .bind("feedback", feedback)
                        .bind("id", attemptId)
                        .execute()
        );
    }

    /**
     * {@inheritDoc}
     * <p>
     * Retrieves the most recent completed attempt ID for a quiz and user.
     * </p>
     */
    @Override
    public Integer getAttemptId(int quizId, int userId) {
        return jdbi.withHandle(handle ->
                handle.createQuery("""
                SELECT attempt_id FROM quiz_attempts
                WHERE is_completed = true AND quiz_id = :quizId AND answered_by = :userId
                ORDER BY attempt_id DESC LIMIT 1
            """)
                        .bind("quizId", quizId)
                        .bind("userId", userId)
                        .mapTo(Integer.class)
                        .findOne()
                        .orElse(null)
        );
    }
}
