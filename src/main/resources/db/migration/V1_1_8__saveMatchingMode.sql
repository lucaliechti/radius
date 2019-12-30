ALTER TABLE matches ADD COLUMN matchingmode varchar(20);
UPDATE matches SET matchingmode = 'REGULAR';
