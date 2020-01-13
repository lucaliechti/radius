CREATE TABLE mentions (
	pkey serial PRIMARY KEY,
	publicationDate date NOT NULL,
	medium varchar(50) NOT NULL,
	link varchar(200) NOT NULL,
	publicationType varchar(20) NOT NULL,
	publicationLanguage varchar(2) NOT NULL,
	uuid varchar(50) NOT NULL
);