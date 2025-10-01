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

public class QuizResultService {
    private final QuizDao quizDao;
    private final QuestionDao questionDao;
    private final OptionDao optionDao;
    private final ResponseDao responseDao;

    public QuizResultService(QuizDao quizDao, QuestionDao questionDao, OptionDao optionDao, ResponseDao responseDao) {
        this.quizDao = quizDao;
        this.questionDao = questionDao;
        this.optionDao = optionDao;
        this.responseDao = responseDao;
    }

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
