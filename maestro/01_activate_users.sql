UPDATE users SET enabled = true;
INSERT INTO authorities (email, authority, datecreate, datemodify) VALUES ('luca.liechti@gmail.com', 'ROLE_ADMIN', NOW(), NOW());
