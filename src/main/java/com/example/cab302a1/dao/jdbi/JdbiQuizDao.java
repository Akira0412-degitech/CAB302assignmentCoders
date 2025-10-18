package com.example.cab302a1.dao.jdbi;

import com.example.cab302a1.DBconnection;
import com.example.cab302a1.dao.QuizDao;
import com.example.cab302a1.model.Quiz;
import org.jdbi.v3.core.Jdbi;

import java.util.List;

/**
 * {@code JdbiQuizDao} provides a JDBI-based implementation of the {@link QuizDao} interface.
 * <p>
 * It performs the same CRUD operations as {@code JdbcQuizDao}, but uses JDBI to
 * simplify SQL handling, resource management, and object mapping.
 * </p>
 */
public class JdbiQuizDao implements QuizDao {

    private final Jdbi jdbi;

    public JdbiQuizDao() {
        this.jdbi = DBconnection.getJdbi();
    }

    /**
     * {@inheritDoc}
     * <p>
     * Retrieves all quiz records along with their author usernames.
     * </p>
     */
    @Override
    public List<Quiz> getAllQuizzes() {
        String sql = """
            SELECT q.quiz_id,
                   q.title,
                   q.description,
                   q.created_by,
                   q.is_Hidden,
                   u.username AS author_username
            FROM quizzes q
            LEFT JOIN users u ON u.user_id = q.created_by
        """;

        return jdbi.withHandle(handle ->
                handle.createQuery(sql)
                        .map((rs, ctx) -> new Quiz(
                                rs.getInt("quiz_id"),
                                rs.getString("title"),
                                rs.getString("description"),
                                rs.getInt("created_by"),
                                rs.getBoolean("is_Hidden"),
                                rs.getString("author_username")
                        ))
                        .list()
        );
    }

    /**
     * {@inheritDoc}
     * <p>
     * Inserts a new quiz and returns the generated quiz_id.
     * </p>
     */
    @Override
    public int insertQuiz(Quiz _quiz) {
        String sql = """
            INSERT INTO quizzes (title, description, created_by, is_Hidden)
            VALUES (:title, :description, :createdBy, false)
        """;

        return jdbi.withHandle(handle ->
                handle.createUpdate(sql)
                        .bind("title", _quiz.getTitle())
                        .bind("description", _quiz.getDescription())
                        .bind("createdBy", _quiz.getCreated_by())
                        .executeAndReturnGeneratedKeys("quiz_id")
                        .mapTo(Integer.class)
                        .findOne()
                        .orElse(-1)
        );
    }

    /**
     * {@inheritDoc}
     * <p>
     * Updates the title and description of an existing quiz.
     * </p>
     */
    @Override
    public void updateQuiz(Quiz _quiz) {
        String sql = """
            UPDATE quizzes
            SET title = :title, description = :description
            WHERE quiz_id = :quizId
        """;

        jdbi.useHandle(handle ->
                handle.createUpdate(sql)
                        .bind("title", _quiz.getTitle())
                        .bind("description", _quiz.getDescription())
                        .bind("quizId", _quiz.getQuizId())
                        .execute()
        );
    }

    /**
     * {@inheritDoc}
     * <p>
     * Retrieves a quiz by its ID, joining the users table for author info.
     * </p>
     */
    @Override
    public Quiz getQuizById(int _quiz_id) {
        String sql = """
            SELECT q.quiz_id,
                   q.title,
                   q.description,
                   q.is_Hidden,
                   q.created_by,
                   u.username
            FROM quizzes q
            JOIN users u ON u.user_id = q.created_by
            WHERE q.quiz_id = :quizId
        """;

        return jdbi.withHandle(handle ->
                handle.createQuery(sql)
                        .bind("quizId", _quiz_id)
                        .map((rs, ctx) -> new Quiz(
                                rs.getInt("quiz_id"),
                                rs.getString("title"),
                                rs.getString("description"),
                                rs.getInt("created_by"),
                                rs.getBoolean("is_Hidden"),
                                rs.getString("username")
                        ))
                        .findOne()
                        .orElse(null)
        );
    }

    /**
     * {@inheritDoc}
     * <p>
     * Updates quiz visibility (is_Hidden flag).
     * </p>
     */
    @Override
    public void updateQuizStatus(int _quizId, boolean _IsHidden) {
        String sql = """
            UPDATE quizzes
            SET is_Hidden = :isHidden
            WHERE quiz_id = :quizId
        """;

        jdbi.useHandle(handle ->
                handle.createUpdate(sql)
                        .bind("isHidden", _IsHidden)
                        .bind("quizId", _quizId)
                        .execute()
        );
    }

    /**
     * Retrieves all quizzes created by a specific teacher.
     */
    @Override
    public List<Quiz> getQuizByTeacherId(int _teacherId) {
        String sql = """
            SELECT q.quiz_id,
                   q.title,
                   q.description,
                   q.created_by,
                   q.is_Hidden,
                   u.username
            FROM quizzes q
            JOIN users u ON q.created_by = u.user_id
            WHERE q.created_by = :teacherId
        """;

        return jdbi.withHandle(handle ->
                handle.createQuery(sql)
                        .bind("teacherId", _teacherId)
                        .map((rs, ctx) -> new Quiz(
                                rs.getInt("quiz_id"),
                                rs.getString("title"),
                                rs.getString("description"),
                                rs.getInt("created_by"),
                                rs.getBoolean("is_Hidden"),
                                rs.getString("username")
                        ))
                        .list()
        );
    }
}
