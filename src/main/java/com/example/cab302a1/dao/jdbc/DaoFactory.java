package com.example.cab302a1.dao.jdbc;

import com.example.cab302a1.dao.*;


/**
 * {@code DaoFactory} provides centralized access to all DAO instances
 * used throughout the application.
 *
 * <p>This class follows the <b>Factory</b> and <b>Singleton</b> design patterns,
 * ensuring that only one instance of each DAO implementation exists.
 * It also handles inter-DAO dependencies such as
 * {@link com.example.cab302a1.dao.ReviewDao} depending on
 * {@link com.example.cab302a1.dao.QuestionDao}.</p>
 *
 * <p>By using this factory, controllers and services can retrieve
 * pre-configured DAO objects without creating new ones repeatedly.</p>
 *
 * <p>Example usage:
 * <pre>{@code
 * UserDao userDao = DaoFactory.getUserDao();
 * QuizDao quizDao = DaoFactory.getQuizDao();
 * ReviewDao reviewDao = DaoFactory.getReviewDao();
 * }</pre>
 */
public final class DaoFactory {

    // Prevent instantiation
    private DaoFactory() {}

    // === Base DAOs ===
    private static final UserDao userDao = new JdbcUserDao();
    private static final QuizDao quizDao = new JdbcQuizDao();
    private static final QuestionDao questionDao = new JdbcQuestionDao();
    private static final ResponseDao responseDao = new JdbcResponseDao();
    private static final OptionDao optionDao = new JdbcOptionDao();


    // === Dependent DAOs ===
    // ReviewDao depends on QuestionDao for calculating total question count
    private static final ReviewDao reviewDao = new JdbcReviewDao(questionDao);
    private static final AttemptDao attemptDao = new JdbcAttemptDao(responseDao);

    // === Factory Getters ===
    public static UserDao getUserDao() {
        return userDao;
    }

    public static QuizDao getQuizDao() {
        return quizDao;
    }

    public static QuestionDao getQuestionDao() {
        return questionDao;
    }

    public static ResponseDao getResponseDao() {
        return responseDao;
    }

    public static OptionDao getOptionDao() {
        return optionDao;
    }

    public static AttemptDao getAttemptDao() {
        return attemptDao;
    }

    public static ReviewDao getReviewDao() {
        return reviewDao;
    }
}