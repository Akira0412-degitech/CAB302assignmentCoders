-- Inserts sample quiz data for testing and demonstration purposes.
-- Adds example Java-related quizzes authored by Teacher (user_id = 2).

INSERT INTO quizzes (title, description, created_by) VALUES
('Java Basics', 'Covers syntax, variables, loops, and conditionals', 2),
('OOP Essentials', 'Covers inheritance, polymorphism, and encapsulation', 2),
('Exception Handling', 'Quiz on try/catch, throws, and custom exceptions', 2);
