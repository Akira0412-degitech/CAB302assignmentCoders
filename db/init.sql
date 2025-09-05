-- init.sql for root user just initialization
CREATE DATABASE IF NOT EXISTS cab302_quizApp
  DEFAULT CHARACTER SET utf8mb4
  DEFAULT COLLATE utf8mb4_general_ci;

CREATE USER IF NOT EXISTS 'appuser'@'localhost' IDENTIFIED BY 'apppass';
GRANT ALL PRIVILEGES ON cab302_quizApp.* TO 'appuser'@'localhost';
FLUSH PRIVILEGES;

-- to run the code above to set up database, run the code below
-- Get-Content .\db\init.sql | mysql -u root -p
-- but before that, mysql path need to be set in a system path to run mysql command.