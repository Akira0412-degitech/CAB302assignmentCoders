package com.example.cab302a1.dao.jdbi;

import com.example.cab302a1.DBconnection;
import com.example.cab302a1.dao.QuestionDao;
import com.example.cab302a1.model.QuizQuestionCreate;
import org.jdbi.v3.core.Jdbi;

import java.util.ArrayList;
import java.util.List;

/**
 * {@code JdbiQuestionDao} provides a JDBI-based implementation of the {@link QuestionDao} interface.
 * <p>
 * Functionally identical to {@code JdbcQuestionDao}, but uses JDBI to simplify SQL operations.
 * </p>
 */
public class JdbiQuestionDao implements QuestionDao {

    private final Jdbi jdbi;

    public JdbiQuestionDao() {
        this.jdbi = DBconnection.getJdbi();
    }

    /**
     * {@inheritDoc}
     * <p>
     * Retrieves all questions for a given quiz.
     * </p>
     */
    @Override
    public List<QuizQuestionCreate> getAllQuestions(int _quiz_id) {
        return jdbi.withHandle(handle ->
                handle.createQuery("""
                SELECT question_id, quiz_id, statement, explanation
                FROM questions
                WHERE quiz_id = :quizId
            """)
                        .bind("quizId", _quiz_id)
                        .map((rs, ctx) -> new QuizQuestionCreate(
                                rs.getInt("question_id"),
                                rs.getInt("quiz_id"),
                                rs.getString("statement"),
                                rs.getString("explanation")
                        ))
                        .list()
        );
    }

    /**
     * {@inheritDoc}
     * <p>
     * Inserts a new question and returns its generated ID.
     * </p>
     */
    @Override
    public int insertQuestion(QuizQuestionCreate q) {

        return jdbi.withHandle(handle ->
                handle.createUpdate("""
                INSERT INTO questions (quiz_id, statement, explanation)
                VALUES (:quizId, :statement, :explanation)
            """)
                        .bind("quizId", q.getQuizId())
                        .bind("statement", q.getQuestionText())
                        .bind("explanation", q.getExplanation())
                        .executeAndReturnGeneratedKeys("question_id")
                        .mapTo(Integer.class)
                        .findOne()
                        .orElse(-1)
        );
    }

    /**
     * {@inheritDoc}
     * <p>
     * Updates an existing question’s text and explanation.
     * </p>
     */
    @Override
    public void updateQuestion(QuizQuestionCreate q) {
        jdbi.useHandle(handle ->
                handle.createUpdate("""
                UPDATE questions
                SET statement = :statement, explanation = :explanation
                WHERE question_id = :questionId
            """)
                        .bind("statement", q.getQuestionText())
                        .bind("explanation", q.getExplanation())
                        .bind("questionId", q.getQuestionId())
                        .execute()
        );
    }

    /**
     * {@inheritDoc}
     * <p>
     * Counts how many questions belong to a given quiz.
     * </p>
     */
    @Override
    public int getNumQuestion(int _quiz_id) {
        return jdbi.withHandle(handle ->
                handle.createQuery("""
                SELECT COUNT(question_id) AS cnt
                FROM questions
                WHERE quiz_id = :quizId
            """)
                        .bind("quizId", _quiz_id)
                        .mapTo(Integer.class)
                        .findOne()
                        .orElse(-1)
        );
    }

    /**
     * {@inheritDoc}
     * <p>
     * Retrieves all question records associated with a given quiz ID.
     * </p>
     */
    @Override
    public List<QuizQuestionCreate> getQuestionsByQuizId(int _quiz_id) {
        return jdbi.withHandle(handle ->
                handle.createQuery("""
                SELECT question_id, quiz_id, statement, explanation
                FROM questions
                WHERE quiz_id = :quizId
            """)
                        .bind("quizId", _quiz_id)
                        .map((rs, ctx) -> new QuizQuestionCreate(
                                rs.getInt("question_id"),
                                rs.getInt("quiz_id"),
                                rs.getString("statement"),
                                rs.getString("explanation")
                        ))
                        .list()
        );
    }
}
