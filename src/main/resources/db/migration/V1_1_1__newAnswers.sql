ALTER TABLE users ADD COLUMN regularanswers varchar(100);
UPDATE users SET regularanswers = replace(replace(CONCAT(q1,';',q2,';',q3,';',q4,';',q5), 't', 'TRUE'), 'f', 'FALSE') WHERE answered = 'TRUE';
ALTER TABLE users DROP COLUMN q1;
ALTER TABLE users DROP COLUMN q2;
ALTER TABLE users DROP COLUMN q3;
ALTER TABLE users DROP COLUMN q4;
ALTER TABLE users DROP COLUMN q5;