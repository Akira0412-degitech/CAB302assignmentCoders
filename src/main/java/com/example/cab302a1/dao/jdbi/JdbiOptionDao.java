package com.example.cab302a1.dao.jdbi;

import com.example.cab302a1.DBconnection;
import com.example.cab302a1.dao.OptionDao;
import com.example.cab302a1.model.QuizChoiceCreate;
import org.jdbi.v3.core.Jdbi;

import java.util.List;

/**
 * {@code JdbiOptionDao} provides a JDBI-based implementation of the {@link OptionDao} interface.
 * <p>
 * This version keeps the same logic as the JDBC version but uses JDBI for cleaner and safer
 * database interaction. It manages CRUD operations for quiz question options on the
 * {@code question_options} table using {@link DBconnection#getJdbi()}.
 * </p>
 */
public class JdbiOptionDao implements OptionDao {

    private final Jdbi jdbi;

    public JdbiOptionDao() {
        this.jdbi = DBconnection.getJdbi();
    }

    /**
     * {@inheritDoc}
     * <p>
     * Inserts a new option into {@code question_options} and returns the generated option_id.
     * Returns {@code -1} if the insertion fails.
     * </p>
     */
    @Override
    public int insertOption(QuizChoiceCreate choice) {
        return jdbi.withHandle(handle ->
                handle.createUpdate("""
                INSERT INTO question_options (question_id, option_text, is_correct)
                VALUES (:questionId, :optionText, :isCorrect)
            """)
                        .bind("questionId", choice.getQuestion_id())
                        .bind("optionText", choice.getText())
                        .bind("isCorrect", choice.isCorrect())
                        .executeAndReturnGeneratedKeys("option_id")
                        .mapTo(Integer.class)
                        .findOne()
                        .orElse(-1)
        );
    }

    /**
     * {@inheritDoc}
     * <p>
     * Retrieves all options associated with a specific question_id.
     * </p>
     */
    @Override
    public List<QuizChoiceCreate> getOptionsByQuestionId(int questionId) {
        return jdbi.withHandle(handle ->
                handle.createQuery("""
                SELECT option_id, question_id, option_text, is_correct
                FROM question_options
                WHERE question_id = :questionId
            """)
                        .bind("questionId", questionId)
                        .map((rs, ctx) -> {
                            QuizChoiceCreate choice = new QuizChoiceCreate();
                            choice.setOption_id(rs.getInt("option_id"));
                            choice.setQuestion_id(rs.getInt("question_id"));
                            choice.setText(rs.getString("option_text"));
                            choice.setIs_correct(rs.getBoolean("is_correct"));
                            return choice;
                        })
                        .list()
        );
    }

    /**
     * {@inheritDoc}
     * <p>
     * Updates an existing option’s text and correctness flag.
     * </p>
     */
    @Override
    public void updateOption(QuizChoiceCreate choice) {
        jdbi.useHandle(handle ->
                handle.createUpdate("""
                UPDATE question_options
                SET option_text = :optionText, is_correct = :isCorrect
                WHERE option_id = :optionId
            """)
                        .bind("optionText", choice.getText())
                        .bind("isCorrect", choice.isCorrect())
                        .bind("optionId", choice.getOption_id())
                        .execute()
        );
    }
}
