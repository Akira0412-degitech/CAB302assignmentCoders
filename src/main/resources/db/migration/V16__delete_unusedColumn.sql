-- V16__delete_unusedColumn.sql

-- Drop column safely from 'questions' table
ALTER TABLE questions
DROP COLUMN type;

-- Drop multiple columns from 'quiz_attempts' table
ALTER TABLE quiz_attempts
DROP COLUMN start_time,
DROP COLUMN end_time;
