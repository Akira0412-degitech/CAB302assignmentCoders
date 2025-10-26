-- Adds a new username column to the users table with safe handling.
-- Ensures existing records receive default or generated usernames before enforcing NOT NULL constraint.

-- First, check if username column exists and drop it if it does (in case of partial failure)
SET @column_exists = (SELECT COUNT(*) FROM INFORMATION_SCHEMA.COLUMNS 
                     WHERE TABLE_SCHEMA = DATABASE() 
                     AND TABLE_NAME = 'users' 
                     AND COLUMN_NAME = 'username');

SET @sql = IF(@column_exists > 0, 'ALTER TABLE users DROP COLUMN username', 'SELECT "Column does not exist"');
PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

-- Now add the username column properly
ALTER TABLE users ADD COLUMN username VARCHAR(100);

-- Update existing users with default usernames
UPDATE users SET username = 'demo_user' WHERE email = 'demo@example.com';
UPDATE users SET username = 'admin_user' WHERE email = 'admin@example.com';

-- Update any remaining rows with generic usernames based on user_id
UPDATE users SET username = CONCAT('user_', user_id) WHERE username IS NULL;

-- Now make the column NOT NULL (but not UNIQUE - only email should be unique)
ALTER TABLE users MODIFY COLUMN username VARCHAR(100) NOT NULL;
