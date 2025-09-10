CREATE TABLE quizzes (
  quiz_id INT PRIMARY KEY AUTO_INCREMENT,
  title VARCHAR(255) NOT NULL,
  description TEXT NOT NULL,
  created_by INT NOT NULL,
  duration_minutes INT DEFAULT NULL,
  max_score INT NOT NULL
);

CREATE TABLE questions (
  question_id INT PRIMARY KEY AUTO_INCREMENT,
  quiz_id INT NOT NULL,
  statement TEXT NOT NULL,
  type ENUM('MCQ','TF'),
  explanation TEXT NOT NULL,
  points INT NOT NULL
);

CREATE TABLE question_options (
  option_id INT PRIMARY KEY AUTO_INCREMENT,
  question_id INT NOT NULL,
  option_text VARCHAR(255) NOT NULL,
  is_correct BOOLEAN DEFAULT FALSE
);

CREATE TABLE quiz_attempts (
  attempt_id INT PRIMARY KEY AUTO_INCREMENT,
  quiz_id INT NOT NULL,
  answered_by INT NOT NULL,
  start_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  end_time TIMESTAMP,
  score INT,
  is_completed BOOLEAN NOT NULL DEFAULT FALSE
);

CREATE TABLE question_responses (
  response_id INT PRIMARY KEY AUTO_INCREMENT,
  attempt_id INT NOT NULL,
  question_id INT NOT NULL,
  option_id INT,
  is_correct BOOLEAN,
  points_earned INT DEFAULT 0
);

CREATE TABLE quiz_assignments (
  assignment_id INT PRIMARY KEY AUTO_INCREMENT,
  quiz_id INT NOT NULL,
  allocated_to INT NOT NULL,
  assigned_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  due_date TIMESTAMP DEFAULT NULL
);

ALTER TABLE quizzes
  ADD CONSTRAINT fk_quizzes_created_by
  FOREIGN KEY (created_by) REFERENCES users(user_id);

ALTER TABLE questions
  ADD CONSTRAINT fk_questions_quiz
  FOREIGN KEY (quiz_id) REFERENCES quizzes(quiz_id);

ALTER TABLE question_options
  ADD CONSTRAINT fk_options_question
  FOREIGN KEY (question_id) REFERENCES questions(question_id);

ALTER TABLE quiz_attempts
  ADD CONSTRAINT fk_attempts_quiz
  FOREIGN KEY (quiz_id) REFERENCES quizzes(quiz_id);

ALTER TABLE quiz_attempts
  ADD CONSTRAINT fk_attempts_user
  FOREIGN KEY (answered_by) REFERENCES users(user_id);

ALTER TABLE question_responses
  ADD CONSTRAINT fk_responses_attempt
  FOREIGN KEY (attempt_id) REFERENCES quiz_attempts(attempt_id);

ALTER TABLE question_responses
  ADD CONSTRAINT fk_responses_question
  FOREIGN KEY (question_id) REFERENCES questions(question_id);

ALTER TABLE question_responses
  ADD CONSTRAINT fk_responses_option
  FOREIGN KEY (option_id) REFERENCES question_options(option_id);

ALTER TABLE quiz_assignments
  ADD CONSTRAINT fk_assignments_quiz
  FOREIGN KEY (quiz_id) REFERENCES quizzes(quiz_id);

ALTER TABLE quiz_assignments
  ADD CONSTRAINT fk_assignments_user
  FOREIGN KEY (allocated_to) REFERENCES users(user_id);
