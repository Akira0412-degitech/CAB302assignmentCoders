ALTER TABLE quizzes
    DROP COLUMN duration_minutes,
    DROP COLUMN max_score;

ALTER TABLE questions
    DROP COLUMN points,
    MODIFY COLUMN explanation TEXT NULL;

ALTER TABLE question_responses
    DROP COLUMN points_earned;
