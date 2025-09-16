
-- Default Data for quiz_attempts table

-- testuser@example.com tried quiz_id = 1 and the score is 16
INSERT INTO quiz_attempts (quiz_id, answered_by, score, is_completed)
VALUES(1, 3, 16, TRUE);
-- quiz_id = 2 and the score is 7
INSERT INTO quiz_attempts (quiz_id, answered_by, score, is_completed)
VALUES(2, 3, 7, TRUE);
-- quiz_id = 3 and the score is 31
INSERT INTO quiz_attempts (quiz_id, answered_by, score, is_completed)
VALUES(3, 3, null, false);

-- demo@example.com tried
INSERT INTO quiz_attempts (quiz_id, answered_by, score, is_completed)
-- quiz_id = 1 and the score is 9
VALUES(1, 1, null, false);
-- quiz_id = 2 and the score is 3
INSERT INTO quiz_attempts (quiz_id, answered_by, score, is_completed)
VALUE(2, 1, 3, TRUE);
-- quiz_id = 3 and the score is 2
INSERT INTO quiz_attempts (quiz_id, answered_by, score, is_completed)
VALUES(3, 1, 2, TRUE);
