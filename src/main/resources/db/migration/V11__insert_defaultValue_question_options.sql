-- Attempt 1 (quiz 1 by user 3, score = 3, all correct)
INSERT INTO question_responses (attempt_id, question_id, option_id, is_correct) VALUES
(1, 1, 1, 1),
(1, 2, 5, 1),
(1, 3, 9, 1);

-- Attempt 2 (quiz 2 by user 3, score = 2, 1 correct 2 wrong)
INSERT INTO question_responses (attempt_id, question_id, option_id, is_correct) VALUES
(2, 4, 14, 1),
(2, 5, 17, 1),
(2, 6, 22, 0);

-- Attempt 5 (quiz 2 by user 1, score = 0 all wrong)
INSERT INTO question_responses (attempt_id, question_id, option_id, is_correct) VALUES
(5, 4, 15, 0),
(5, 5, 20, 0),
(5, 6, 22, 0);

-- Attempt 6 (quiz 3 by user 1, score = 2 → 2 correct, 1 wrong)
INSERT INTO question_responses (attempt_id, question_id, option_id, is_correct) VALUES
(6, 7, 25, 1),
(6, 8, 30, 1),
(6, 9, 33, 0);
