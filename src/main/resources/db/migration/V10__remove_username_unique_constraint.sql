-- Remove UNIQUE constraint from username column
-- Username should not be unique, only email should be unique

-- Drop the unique constraint on username
ALTER TABLE users DROP CONSTRAINT uk_users_username;
