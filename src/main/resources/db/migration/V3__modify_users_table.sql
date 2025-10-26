-- Migration V3__Update_users_table.sql
-- ------------------------------------
-- Updates the users table to ensure all records have a valid role.
-- Sets missing roles to 'Student' and enforces NOT NULL and DEFAULT constraints.
-- Also ensures 'created_at' always stores a non-null timestamp.


-- Update existing value where role is null
UPDATE users set role = 'Student' WHERE role IS NULL;
-- Update current database schema
ALTER TABLE users
  MODIFY COLUMN role ENUM('Teacher','Student') NOT NULL DEFAULT 'Student',
  MODIFY COLUMN created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP;