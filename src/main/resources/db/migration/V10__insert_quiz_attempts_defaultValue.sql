
-- Default Data for quiz_attempts table

-- testuser@example.com tried quiz_id = 1 and the score is 3
INSERT INTO quiz_attempts (quiz_id, answered_by, score, is_completed)
VALUES(1, 3, 3, TRUE);
-- quiz_id = 2 and the score is 2
INSERT INTO quiz_attempts (quiz_id, answered_by, score, is_completed)
VALUES(2, 3, 2, TRUE);
-- quiz_id = 3 and yet completed
INSERT INTO quiz_attempts (quiz_id, answered_by, score, is_completed)
VALUES(3, 3, null, TRUE);

-- demo@example.com tried
INSERT INTO quiz_attempts (quiz_id, answered_by, score, is_completed)
-- quiz_id = 1 and yet completed
VALUES(1, 1, null, TRUE);
-- quiz_id = 2 and the score is 0
INSERT INTO quiz_attempts (quiz_id, answered_by, score, is_completed)
VALUE(2, 1, 0, TRUE);
-- quiz_id = 3 and the score is 2
INSERT INTO quiz_attempts (quiz_id, answered_by, score, is_completed)
VALUES(3, 1, 2, TRUE);
