-- Removes unused columns from quizzes, questions, and question_responses tables.
-- Drops duration, max_score, and points-related fields to streamline schema design.
-- Makes question explanations optional (NULL allowed).

ALTER TABLE quizzes
    DROP COLUMN duration_minutes,
    DROP COLUMN max_score;

ALTER TABLE questions
    DROP COLUMN points,
    MODIFY COLUMN explanation TEXT NULL;

ALTER TABLE question_responses
    DROP COLUMN points_earned;
