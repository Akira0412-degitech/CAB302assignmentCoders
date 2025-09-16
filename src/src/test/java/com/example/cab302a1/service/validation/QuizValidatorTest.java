//Test

package com.example.cab302a1.service.validation;


import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class QuizValidatorTest {

    private QuizDraft validDraft() {
        QuizDraft d = new QuizDraft();
        d.setTitle("title");
        d.setDescription("desc");

        QuizDraft.QuestionDraft q = new QuizDraft.QuestionDraft();
        q.setText("Q1");
        q.setChoices(Arrays.asList("A", "B", "C", "D"));
        q.setCorrectIndex(1);

        d.getQuestions().add(q);
        return d;
    }

    @Test
    @DisplayName("Normal input: no errors")
    void ok() {
        List<String> errors = QuizValidator.validate(validDraft());
        assertTrue(errors.isEmpty());
    }

    @Test
    @DisplayName("Question text is empty: Error")
    void questionTextEmpty() {
        QuizDraft d = validDraft();
        d.getQuestions().get(0).setText("  ");
        List<String> errors = QuizValidator.validate(d);
        assertTrue(errors.contains("Q#1_TEXT_EMPTY"));
    }

    @Test
    @DisplayName("There are 4 blanks in the view: Includes errors")
    void choicesBlank() {
        QuizDraft d = validDraft();
        d.getQuestions().get(0).setChoices(Arrays.asList("A", "", "C", ""));
        List<String> errors = QuizValidator.validate(d);
        assertTrue(errors.contains("Q#1_CHOICE_2_EMPTY"));
        assertTrue(errors.contains("Q#1_CHOICE_4_EMPTY"));
    }

    @Test
    @DisplayName("Number of views is not 4: Error")
    void choicesCountNotFour() {
        QuizDraft d = validDraft();
        d.getQuestions().get(0).setChoices(Arrays.asList("A", "B", "C")); // 3answer
        List<String> errors = QuizValidator.validate(d);
        assertTrue(errors.contains("Q#1_CHOICES_NEED_4"));
    }

    @Test
    @DisplayName("No correct answer selected: Error")
    void noCorrectSelected() {
        QuizDraft d = validDraft();
        d.getQuestions().get(0).setCorrectIndex(null);
        List<String> errors = QuizValidator.validate(d);
        assertTrue(errors.contains("Q#1_NO_CORRECT"));
    }
}
