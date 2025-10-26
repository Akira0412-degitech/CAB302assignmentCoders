-- Adds is_Hidden column to quizzes table.
-- Enables teachers to hide or unpublish quizzes from student view.


ALTER TABLE quizzes
ADD COLUMN is_Hidden BOOLEAN NOT NULL DEFAULT FALSE;