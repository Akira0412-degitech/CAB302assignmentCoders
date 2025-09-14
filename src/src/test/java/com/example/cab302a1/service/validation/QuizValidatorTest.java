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
    @DisplayName("정상 입력: 에러 없음")
    void ok() {
        List<String> errors = QuizValidator.validate(validDraft());
        assertTrue(errors.isEmpty());
    }

    @Test
    @DisplayName("질문 텍스트 비어있음 → 에러")
    void questionTextEmpty() {
        QuizDraft d = validDraft();
        d.getQuestions().get(0).setText("  ");
        List<String> errors = QuizValidator.validate(d);
        assertTrue(errors.contains("Q#1_TEXT_EMPTY"));
    }

    @Test
    @DisplayName("보기 4개 중 공란 존재 → 에러들 포함")
    void choicesBlank() {
        QuizDraft d = validDraft();
        d.getQuestions().get(0).setChoices(Arrays.asList("A", "", "C", ""));
        List<String> errors = QuizValidator.validate(d);
        assertTrue(errors.contains("Q#1_CHOICE_2_EMPTY"));
        assertTrue(errors.contains("Q#1_CHOICE_4_EMPTY"));
    }

    @Test
    @DisplayName("보기 개수가 4개가 아님 → 에러")
    void choicesCountNotFour() {
        QuizDraft d = validDraft();
        d.getQuestions().get(0).setChoices(Arrays.asList("A", "B", "C")); // 3개
        List<String> errors = QuizValidator.validate(d);
        assertTrue(errors.contains("Q#1_CHOICES_NEED_4"));
    }

    @Test
    @DisplayName("정답 미선택 → 에러")
    void noCorrectSelected() {
        QuizDraft d = validDraft();
        d.getQuestions().get(0).setCorrectIndex(null);
        List<String> errors = QuizValidator.validate(d);
        assertTrue(errors.contains("Q#1_NO_CORRECT"));
    }
}
