-- init.sql for root user just initialization
CREATE DATABASE IF NOT EXISTS cab302_quizapp
  DEFAULT CHARACTER SET utf8mb4
  DEFAULT COLLATE utf8mb4_general_ci;

CREATE USER IF NOT EXISTS 'appuser'@'localhost' IDENTIFIED BY 'AppPass#2025';
GRANT ALL PRIVILEGES ON cab302_quizapp.* TO 'appuser'@'localhost';
FLUSH PRIVILEGES;

-- To set up the database with this script, run one of the following commands:
--
-- On Windows:
--   Get-Content .\db\init.sql | mysql -u root -p
-- On macOS / Linux:
--   mysql -u root -p < db/init.sql
-- Note:
--   Make sure the MySQL client (`mysql` command) is available in your system PATH
--   before running these commands.
