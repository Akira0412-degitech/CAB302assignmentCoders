-- Update existing value where role is null
UPDATE users set role = 'Student' WHERE role IS NULL;
-- Update current database schema
ALTER TABLE users
  MODIFY COLUMN role ENUM('Teacher','Student') NOT NULL DEFAULT 'Student',
  MODIFY COLUMN created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP;