-- Allows quiz descriptions to be optional (nullable) instead of mandatory.
ALTER TABLE quizzes
  MODIFY COLUMN description TEXT NULL;
