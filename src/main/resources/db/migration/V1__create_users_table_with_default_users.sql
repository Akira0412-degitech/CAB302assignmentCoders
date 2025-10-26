-- Creates the 'users' table and inserts demo user accounts for testing.
-- Includes basic fields: user_id, email, password, role, and creation timestamp.
-- Provides one Student and one Teacher account for initial login testing.


CREATE TABLE users (
  user_id INT PRIMARY KEY AUTO_INCREMENT,
  email VARCHAR(255) NOT NULL UNIQUE,
  password VARCHAR(255) NOT NULL,
  role ENUM('Teacher', 'Student'),
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

INSERT INTO users (email, password, role)
VALUES ('demo@example.com', 'demo', 'Student'),
       ('admin@example.com', '1234', 'Teacher');
