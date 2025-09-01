DROP DATABASE if EXISTS cab302_quizApp
CREATE DATABASE cab302_quizApp
  DEFAULT CHARACTER SET utf8mb4
  DEFAULT COLLATE utf8mb4_general_ci;

CREATE USER IF NOT EXISTS 'appuser'@'localhost' IDENTIFIED BY 'apppass';
GRANT ALL PRIVILEGES ON cab302_quizApp.* TO 'appuser'@'localhost';
FLUSH PRIVILEGES;

USE cab302_quizApp;

CREATE TABLE IF NOT EXISTS users (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  email VARCHAR(255) NOT NULL UNIQUE,
  password VARCHAR(255) NOT NULL,
  role ENUM("Teacher", "Student"),
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

INSERT  INTO users (email, password)
VALUES ('demo@example.com', 'demo');
INSERT INTO users(email, password)
VALUES ("admin", "1234");

-- to run the code above to set up database, run the code below
-- Get-Content .\db\init.sql | mysql -u root -p
-- but before that, mysql path need to be set in a system path to run mysql command.