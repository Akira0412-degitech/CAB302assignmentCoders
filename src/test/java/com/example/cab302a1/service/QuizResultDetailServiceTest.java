package com.example.cab302a1.service;

import com.example.cab302a1.dao.OptionDao;
import com.example.cab302a1.dao.QuestionDao;
import com.example.cab302a1.dao.QuizDao;
import com.example.cab302a1.dao.ResponseDao;
import com.example.cab302a1.model.Quiz;
import com.example.cab302a1.model.QuizQuestionCreate;
import com.example.cab302a1.result.ResultDetail;
import com.example.cab302a1.result.ResultQuestion;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import com.example.cab302a1.model.QuizChoiceCreate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class QuizResultDetailServiceTest {

    private QuizDao quizDao;
    private QuestionDao questionDao;
    private OptionDao optionDao;
    private ResponseDao responseDao;
    private QuizResultDetailService service;

    @BeforeEach
    void setUp() {
        // Create mock objects instead of real DAO or DB
        quizDao = mock(QuizDao.class);
        questionDao = mock(QuestionDao.class);
        optionDao = mock(OptionDao.class);
        responseDao = mock(ResponseDao.class);

        // Inject mocks into the service
        service = new QuizResultDetailService(quizDao, questionDao, optionDao, responseDao);
    }

    @Test
    void getResultDetail_returnsResultDetailWithQuestionsAndResponses() {
        // Arrange - simulate DAO returns
        Quiz quiz = new Quiz();
        quiz.setQuizId(1);
        quiz.setTitle("Sample Quiz");

        QuizQuestionCreate q1 = new QuizQuestionCreate();
        q1.setQuestionId(101);

        QuizChoiceCreate option1 = new QuizChoiceCreate();
        option1.setOption_id(1);
        option1.setText("Option A");

        QuizChoiceCreate option2 = new QuizChoiceCreate();
        option2.setOption_id(2);
        option2.setText("Option B");

        when(quizDao.getQuizById(1)).thenReturn(quiz);
        when(questionDao.getQuestionsByQuizId(1)).thenReturn(List.of(q1));
        when(optionDao.getOptionsByQuestionId(101))
                .thenReturn(List.of(option1, option2));
        when(responseDao.getChosenOptionId(5, 101)).thenReturn(2);

        // Act
        ResultDetail result = service.getResultDetail(5, 1);

        // Assert
        assertNotNull(result);
        assertEquals("Sample Quiz", result.getQuiz().getTitle());
        assertEquals(1, result.getResultQuestions().size());
        ResultQuestion rq = result.getResultQuestions().get(0);
        assertEquals(2, rq.getChosenOption_id());

        // Verify correct DAO calls
        verify(quizDao).getQuizById(1);
        verify(questionDao).getQuestionsByQuizId(1);
        verify(optionDao).getOptionsByQuestionId(101);
        verify(responseDao).getChosenOptionId(5, 101);
    }
}
