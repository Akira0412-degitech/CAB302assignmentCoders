package com.example.cab302a1.service;

import com.example.cab302a1.dao.OptionDao;
import com.example.cab302a1.dao.QuestionDao;
import com.example.cab302a1.dao.jdbc.JdbcOptionDao;
import com.example.cab302a1.model.Quiz;
import com.example.cab302a1.model.QuizChoiceCreate;
import com.example.cab302a1.model.QuizQuestionCreate;

import java.util.List;

public class QuizService {
    private final QuestionDao questionDao = new QuestionDao();
    private final OptionDao optionDao = new JdbcOptionDao();


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
