CREATE DATABASE IF NOT EXISTS practice_cab302
  DEFAULT CHARACTER SET utf8mb4
  DEFAULT COLLATE utf8mb4_general_ci;

CREATE USER IF NOT EXISTS 'appuser'@'localhost' IDENTIFIED BY 'apppass';
GRANT ALL PRIVILEGES ON practice_cab302.* TO 'appuser'@'localhost';
FLUSH PRIVILEGES;

USE practice_cab302;

CREATE TABLE IF NOT EXISTS users (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  email VARCHAR(255) NOT NULL UNIQUE,
  password_hash VARCHAR(255) NOT NULL,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

INSERT IGNORE INTO users (email, password_hash)
VALUES ('demo@example.com', 'demo');

-- to run the code above to set up database, run the code below
-- Get-Content .\db\init.sql | mysql -u root -p
-- but before that, mysql path need to be set in a system path to run mysql command.