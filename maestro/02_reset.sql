DELETE FROM authorities;
DELETE FROM votes;
DELETE FROM matches;
DELETE FROM users;
UPDATE configuration SET value = 'false' WHERE key = 'matching.active';