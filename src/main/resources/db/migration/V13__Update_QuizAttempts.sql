-- Adds a feedback column to quiz_attempts table.
-- Allows teachers to store feedback comments for each student attempt.


ALTER TABLE quiz_attempts
ADD COLUMN feedback VARCHAR(500) NULL;