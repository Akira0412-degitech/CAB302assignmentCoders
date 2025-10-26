-- Updates default user passwords with securely hashed (bcrypt) values.
-- Ensures demo, admin, and test accounts use encrypted credentials.


-- Update already existing password for default user to new one which is demo after encryption.
UPDATE users
SET password = '$2a$10$u.dS5fTJ2QTvHkTRKgdVGONVCgwYiWBdQ231kgBFvQxFdpUQcWica'
WHERE email = 'demo@example.com';

-- Update already existing password for default user to new one which is 1234 after encryption.
UPDATE users
SET password = '$2a$10$hh0x1EnzvySljcK1FreZre5BbQZdXpTDaoGpaowWJg/egSuHZ6c66'
WHERE email = 'admin@example.com';
-- Update already existing password for default user to new one which is testpass after encryption.

UPDATE users
SET password = '$2a$10$PsW9GvkQXpVyErlPjGVkAuvncovji47D6qlYjuJ7fqpkltDXt/xSa'
WHERE email = 'testuser@example.com';
