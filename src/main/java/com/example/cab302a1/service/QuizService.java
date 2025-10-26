package com.example.cab302a1.service;

import com.example.cab302a1.dao.OptionDao;
import com.example.cab302a1.dao.QuestionDao;
import com.example.cab302a1.dao.DaoFactory;
import com.example.cab302a1.model.Quiz;
import com.example.cab302a1.model.QuizChoiceCreate;
import com.example.cab302a1.model.QuizQuestionCreate;

import java.util.List;

/**
 * Service class responsible for loading and assembling complete quiz data
 * by combining quiz information with its questions and answer options.
 * <p>
 * This service acts as a business logic layer that orchestrates multiple
 * DAO operations to provide fully populated Quiz objects ready for use
 * in quiz-taking or review scenarios.
 * </p>
 */
public class QuizService {
    private final QuestionDao questionDao = DaoFactory.getQuestionDao();
    private final OptionDao optionDao = DaoFactory.getOptionDao();

    /**
     * Loads a quiz with all its questions and corresponding answer options.
     * <p>
     * This method takes a basic Quiz object and enriches it with full question
     * data including all available choices/options for each question. It performs
     * the following steps:
     * </p>
     * <ol>
     *   <li>Retrieves all questions associated with the quiz ID</li>
     *   <li>For each question, fetches all available answer options</li>
     *   <li>Attaches the options to their respective questions</li>
     *   <li>Sets the complete question list on the quiz object</li>
     * </ol>
     *
     * @param quiz the Quiz object to be populated with questions and options
     * @return the same Quiz object, now fully populated with questions and choices
     */
    public Quiz loadQuizFully(Quiz quiz) {
        List<QuizQuestionCreate> questions = questionDao.getAllQuestions(quiz.getQuizId());
        for (QuizQuestionCreate q : questions) {
            List<QuizChoiceCreate> options = optionDao.getOptionsByQuestionId(q.getQuestionId());
            q.getChoices().clear();
            q.getChoices().addAll(options);
        }
        quiz.setQuestions(questions);
        return quiz;
    }
}
