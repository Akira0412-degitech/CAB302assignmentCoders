-- init.sql for root user just initialization
CREATE DATABASE IF NOT EXISTS cab302_quizapp
  DEFAULT CHARACTER SET utf8mb4
  DEFAULT COLLATE utf8mb4_general_ci;

CREATE USER IF NOT EXISTS 'appuser'@'localhost' IDENTIFIED BY 'AppPass#2025';
GRANT ALL PRIVILEGES ON cab302_quizApp.* TO 'appuser'@'localhost';
FLUSH PRIVILEGES;

-- to run the code above to set up database, run the code below
-- Get-Content .\db\init.sql | mysql -u root -p
-- or
-- mysql -u root -p < db/init.sql
-- for mac/linux user
-- but before that, mysql command need to be available to use.