package com.example.cab302a1.dao.jdbi;

import com.example.cab302a1.DBconnection;
import com.example.cab302a1.dao.ResponseDao;
import com.example.cab302a1.model.QuestionResponse;
import org.jdbi.v3.core.Jdbi;

import java.util.List;

/**
 * {@code JdbiResponseDao} provides a JDBI-based implementation of the {@link ResponseDao} interface.
 * <p>
 * It manages persistence of quiz attempt responses using JDBI for cleaner, safer,
 * and more efficient database interactions while maintaining the same behavior
 * as the original {@code JdbcResponseDao}.
 * </p>
 */
public class JdbiResponseDao implements ResponseDao {

    private final Jdbi jdbi;

    public JdbiResponseDao() {
        this.jdbi = DBconnection.getJdbi();
    }

    /**
     * {@inheritDoc}
     * <p>
     * Performs a batch insert into {@code question_responses} for improved performance.
     * Skips execution if the provided list is empty or null.
     * </p>
     */
    @Override
    public void saveResponse(int _attemptId, List<QuestionResponse> _response) {
        if (_response == null || _response.isEmpty()) {
            return; // 👈 Skip DB access if nothing to save
        }

        String sql = """
            INSERT INTO question_responses (attempt_id, question_id, option_id, is_correct)
            VALUES (:attemptId, :questionId, :optionId, :isCorrect)
        """;

        jdbi.useHandle(handle -> {
            var batch = handle.prepareBatch(sql);
            for (QuestionResponse r : _response) {
                batch
                        .bind("attemptId", _attemptId)
                        .bind("questionId", r.getQuestion_id())
                        .bind("optionId", r.getOption_id())
                        .bind("isCorrect", r.getIs_Correct())
                        .add();
            }
            batch.execute();
        });
    }

    /**
     * {@inheritDoc}
     * <p>
     * Counts the number of correct responses for a given attempt ID.
     * Returns -1 if an error occurs or no data found.
     * </p>
     */
    @Override
    public int calculateScoreFromResponses(int _attemptId) {
        String sql = """
            SELECT COUNT(response_id) AS score
            FROM question_responses
            WHERE is_correct = TRUE AND attempt_id = :attemptId
        """;

        return jdbi.withHandle(handle ->
                handle.createQuery(sql)
                        .bind("attemptId", _attemptId)
                        .mapTo(Integer.class)
                        .findOne()
                        .orElse(-1)
        );
    }

    /**
     * {@inheritDoc}
     * <p>
     * Retrieves the chosen option_id for a given question within an attempt.
     * Returns -1 if no record exists.
     * </p>
     */
    @Override
    public int getChosenOptionId(int _attempt_id, int _question_id) {
        String sql = """
            SELECT option_id
            FROM question_responses
            WHERE attempt_id = :attemptId AND question_id = :questionId
        """;

        return jdbi.withHandle(handle ->
                handle.createQuery(sql)
                        .bind("attemptId", _attempt_id)
                        .bind("questionId", _question_id)
                        .mapTo(Integer.class)
                        .findOne()
                        .orElse(-1)
        );
    }
}
