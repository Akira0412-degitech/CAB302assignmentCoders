package com.example.cab302a1.util;

import com.example.cab302a1.model.Quiz;
import com.example.cab302a1.model.QuizChoiceCreate;
import com.example.cab302a1.model.QuizQuestionCreate;

import java.util.List;

// BUILT from MAGIC
public final class DemoDataFactory {
    private DemoDataFactory() {}

    public static Quiz demoQuizA(String title) {

        Quiz quiz = new Quiz();
        quiz.setTitle(title);
        quiz.setDescription("Demo description...");

        quiz.getQuestions().add(q1());
        quiz.getQuestions().add(q2());
        return quiz;
    }


    public static Quiz demoQuizB(String title) {
        Quiz quiz = new Quiz(null, null, null);
        quiz.setTitle(title);
        quiz.setDescription("Demo description...");

        quiz.getQuestions().add(q1());
        quiz.getQuestions().add(q2());
        return quiz;
    }

    // dummy
    private static QuizQuestionCreate q1() {
        return new QuizQuestionCreate(
                "Question 1...",
                List.of(
                        new QuizChoiceCreate("Answer 1", false),
                        new QuizChoiceCreate("Answer 2", true),
                        new QuizChoiceCreate("Answer 3", false),
                        new QuizChoiceCreate("Answer 4", false)
                ),
                "Explanation 1"
        );
    }

    private static QuizQuestionCreate q2() {
        return new QuizQuestionCreate(
                "Question 2...",
                List.of(
                        new QuizChoiceCreate("A", true),
                        new QuizChoiceCreate("B", false),
                        new QuizChoiceCreate("C", false),
                        new QuizChoiceCreate("D", false)
                ),
                "Explanation 2"
        );
    }
}
