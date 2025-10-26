package com.example.cab302a1.service;

import com.example.cab302a1.dao.OptionDao;
import com.example.cab302a1.dao.QuestionDao;
import com.example.cab302a1.dao.QuizDao;
import com.example.cab302a1.dao.ResponseDao;
import com.example.cab302a1.model.Quiz;
import com.example.cab302a1.model.QuizQuestionCreate;
import com.example.cab302a1.result.ResultDetail;
import com.example.cab302a1.result.ResultQuestion;

import java.util.ArrayList;
import java.util.List;

/**
 * Service class responsible for assembling detailed quiz result information
 * by combining data from multiple DAOs.
 * <p>
 * This class retrieves quiz metadata, questions, available options, and the
 * student's selected responses to build a complete {@link ResultDetail} object.
 * It acts as a business logic layer that connects the underlying DAO classes
 * and aggregates their data for use in result review views.
 * </p>
 *
 * <p><b>Responsibilities:</b></p>
 * <ul>
 *   <li>Fetches quiz data via {@link QuizDao}</li>
 *   <li>Retrieves related questions via {@link QuestionDao}</li>
 *   <li>Populates question options via {@link OptionDao}</li>
 *   <li>Fetches student's selected answers via {@link ResponseDao}</li>
 *   <li>Combines all into a {@link ResultDetail} containing {@link ResultQuestion} entries</li>
 * </ul>
 *
 * <p>
 * Example usage:
 * <pre>{@code
 * QuizResultDetailService service = new QuizResultDetailService(quizDao, questionDao, optionDao, responseDao);
 * ResultDetail result = service.getResultDetail(5, 10);
 * }</pre>
 * </p>
 */
public class QuizResultDetailService {
    private final QuizDao quizDao;
    private final QuestionDao questionDao;
    private final OptionDao optionDao;
    private final ResponseDao responseDao;

    /**
     * Constructs a {@code QuizResultDetailService} with the required DAO dependencies.
     *
     * @param quizDao      the DAO used for accessing quiz information
     * @param questionDao  the DAO used for retrieving quiz questions
     * @param optionDao    the DAO used for fetching options for each question
     * @param responseDao  the DAO used for obtaining student responses
     */
    public QuizResultDetailService(QuizDao quizDao, QuestionDao questionDao, OptionDao optionDao, ResponseDao responseDao) {
        this.quizDao = quizDao;
        this.questionDao = questionDao;
        this.optionDao = optionDao;
        this.responseDao = responseDao;
    }

    /**
     * Builds a detailed result view for a given quiz attempt by combining quiz,
     * question, option, and response data.
     *
     * <p>
     * The method performs the following steps:
     * </p>
     * <ol>
     *   <li>Retrieves quiz details by quiz ID</li>
     *   <li>Fetches all related questions</li>
     *   <li>Attaches available options to each question</li>
     *   <li>Retrieves the chosen option for each question based on attempt ID</li>
     *   <li>Constructs a {@link ResultDetail} containing {@link ResultQuestion} objects</li>
     * </ol>
     *
     * @param attemptId the unique identifier of the student's quiz attempt
     * @param quizId    the unique identifier of the quiz
     * @return a {@link ResultDetail} object containing quiz info and answered questions
     */
    public ResultDetail getResultDetail(int attemptId, int quizId) {
        Quiz quiz = quizDao.getQuizById(quizId);
        List<QuizQuestionCreate> questions = questionDao.getQuestionsByQuizId(quizId);

        for (QuizQuestionCreate q : questions) {
            q.setOptions(optionDao.getOptionsByQuestionId(q.getQuestionId()));
        }

        List<ResultQuestion> resultQuestions = new ArrayList<>();
        for (QuizQuestionCreate q : questions) {
            int chosenOptionId = responseDao.getChosenOptionId(attemptId, q.getQuestionId());
            ResultQuestion rq = new ResultQuestion(q, chosenOptionId);
            resultQuestions.add(rq);
        }

        return new ResultDetail(quiz, resultQuestions);
    }
}
