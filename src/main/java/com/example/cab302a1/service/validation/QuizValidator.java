package com.example.cab302a1.service.validation;

import java.util.ArrayList;
import java.util.List;

public final class QuizValidator {

    public static final String TITLE_EMPTY        = "TITLE_EMPTY";
    public static final String NO_QUESTIONS       = "NO_QUESTIONS";
    public static final String Q_TEXT_EMPTY       = "Q#%d_TEXT_EMPTY";
    public static final String Q_CHOICES_NEED_4   = "Q#%d_CHOICES_NEED_4";
    public static final String Q_CHOICE_EMPTY     = "Q#%d_CHOICE_%d_EMPTY";
    public static final String Q_NO_CORRECT       = "Q#%d_NO_CORRECT";

    private QuizValidator() {}

    public static List<String> validate(QuizDraft draft) {
        List<String> errors = new ArrayList<>();

        if (isBlank(draft.getTitle())) {
            errors.add(TITLE_EMPTY);
        }
        if (draft.getQuestions() == null || draft.getQuestions().isEmpty()) {
            errors.add(NO_QUESTIONS);
            return errors;
        }

        int qNo = 1;
        for (QuizDraft.QuestionDraft q : draft.getQuestions()) {
            if (isBlank(q.getText())) {
                errors.add(String.format(Q_TEXT_EMPTY, qNo));
            }
            List<String> choices = q.getChoices();
            if (choices == null || choices.size() != 4) {
                errors.add(String.format(Q_CHOICES_NEED_4, qNo));
            } else {
                for (int i = 0; i < 4; i++) {
                    String c = choices.get(i);
                    if (isBlank(c)) {
                        errors.add(String.format(Q_CHOICE_EMPTY, qNo, i + 1));
                    }
                }
            }
            Integer ci = q.getCorrectIndex();
            if (ci == null || ci < 0 || ci > 3) {
                errors.add(String.format(Q_NO_CORRECT, qNo));
            }
            qNo++;
        }
        return errors;
    }

    private static boolean isBlank(String s) {
        return s == null || s.trim().isEmpty();
    }
}
