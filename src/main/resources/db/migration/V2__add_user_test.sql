-- Inserts a test user record for validation and UI testing.
-- Provides a Student account (testuser@example.com / testpass) for local development.


INSERT INTO users(email, password, role)
VALUES ("testuser@example.com", "testpass", "Student")