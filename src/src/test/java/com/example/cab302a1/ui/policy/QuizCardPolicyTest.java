package com.example.cab302a1.ui.policy;

import com.example.cab302a1.UserRole;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class QuizCardPolicyTest {

    @Test
    void teacherSeesEdit() {
        assertTrue(QuizCardPolicy.showEditButton(UserRole.TEACHER));
    }

    @Test
    void studentNoEdit() {
        assertFalse(QuizCardPolicy.showEditButton(UserRole.STUDENT));
    }
}
