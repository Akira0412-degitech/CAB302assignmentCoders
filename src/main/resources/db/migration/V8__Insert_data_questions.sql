-- Inserts sample questions and multiple-choice options for all demo quizzes.
-- Populates data for "Java Basics", "OOP Essentials", and "Exception Handling" quizzes.

-- Java Basics Quiz (quiz_id = 1)
INSERT INTO questions (quiz_id, statement, type, explanation) VALUES
(1, 'Which keyword is used to declare a variable in Java?', 'MCQ', 'Variables are declared using type keywords such as int, double, or String.'),
(1, 'Which symbol is used to end a statement in Java?', 'MCQ', 'Every statement must end with a semicolon (;).'),
(1, 'Which method is the entry point for a Java program?', 'MCQ', 'The main method is the standard entry point.');

-- Options for Java Basics (assuming auto-increment IDs start at 1,2,3)
INSERT INTO question_options (question_id, option_text, is_correct) VALUES
-- Q1 options
(1, 'int', TRUE),
(1, 'var', FALSE),
(1, 'declare', FALSE),
(1, 'let', FALSE),

-- Q2 options
(2, ';', TRUE),
(2, '.', FALSE),
(2, ':', FALSE),
(2, ',', FALSE),

-- Q3 options
(3, 'public static void main(String[] args)', TRUE),
(3, 'start()', FALSE),
(3, 'init()', FALSE),
(3, 'run()', FALSE);


-- OOP Essentials Quiz (quiz_id = 2)
INSERT INTO questions (quiz_id, statement, type, explanation) VALUES
(2, 'Which OOP principle allows a subclass to provide its own method implementation?', 'MCQ', 'This is polymorphism through overriding.'),
(2, 'Which keyword is used for class inheritance in Java?', 'MCQ', 'The extends keyword enables inheritance.'),
(2, 'Which OOP concept hides internal details and shows only functionality?', 'MCQ', 'This is abstraction.');

-- Options for OOP (IDs 4,5,6)
INSERT INTO question_options (question_id, option_text, is_correct) VALUES
-- Q4
(4, 'Encapsulation', FALSE),
(4, 'Polymorphism', TRUE),
(4, 'Abstraction', FALSE),
(4, 'Inheritance', FALSE),

-- Q5
(5, 'extends', TRUE),
(5, 'implements', FALSE),
(5, 'inherits', FALSE),
(5, 'super', FALSE),

-- Q6
(6, 'Abstraction', TRUE),
(6, 'Encapsulation', FALSE),
(6, 'Polymorphism', FALSE),
(6, 'Composition', FALSE);


-- Exception Handling Quiz (quiz_id = 3)
INSERT INTO questions (quiz_id, statement, type, explanation) VALUES
(3, 'Which block must follow a try block in Java?', 'MCQ', 'A try must be followed by either a catch or a finally block.'),
(3, 'Which keyword is used in a method declaration to specify exceptions?', 'MCQ', 'The throws keyword is used to declare exceptions.'),
(3, 'Which exception occurs when accessing an invalid array index?', 'MCQ', 'ArrayIndexOutOfBoundsException is thrown for invalid indices.');

-- Options for Exceptions (IDs 7,8,9)
INSERT INTO question_options (question_id, option_text, is_correct) VALUES
-- Q7
(7, 'catch or finally', TRUE),
(7, 'throw', FALSE),
(7, 'exit', FALSE),
(7, 'break', FALSE),

-- Q8
(8, 'throw', FALSE),
(8, 'throws', TRUE),
(8, 'catch', FALSE),
(8, 'final', FALSE),

-- Q9
(9, 'NullPointerException', FALSE),
(9, 'IndexOutOfBoundsException', FALSE),
(9, 'ArrayIndexOutOfBoundsException', TRUE),
(9, 'RuntimeException', FALSE);
