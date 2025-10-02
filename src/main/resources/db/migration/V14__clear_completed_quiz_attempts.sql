-- Clear completed quiz attempts to allow fresh testing
-- This removes the test data that was showing quizzes as already completed

DELETE FROM question_responses 
WHERE attempt_id IN (
    SELECT attempt_id FROM quiz_attempts WHERE is_completed = TRUE
);

DELETE FROM quiz_attempts WHERE is_completed = TRUE;

