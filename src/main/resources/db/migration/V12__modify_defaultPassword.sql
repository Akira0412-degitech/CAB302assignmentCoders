UPDATE users
SET password = '$2a$10$u.dS5fTJ2QTvHkTRKgdVGONVCgwYiWBdQ231kgBFvQxFdpUQcWica'
WHERE email = 'demo@example.com';

UPDATE users
SET password = '$2a$10$hh0x1EnzvySljcK1FreZre5BbQZdXpTDaoGpaowWJg/egSuHZ6c66'
WHERE email = 'admin@example.com';

UPDATE users
SET password = '$2a$10$PsW9GvkQXpVyErlPjGVkAuvncovji47D6qlYjuJ7fqpkltDXt/xSa'
WHERE email = 'testuser@example.com';
