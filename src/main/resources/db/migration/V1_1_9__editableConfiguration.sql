CREATE TABLE configuration(pkey serial PRIMARY KEY, key varchar(50) UNIQUE, value varchar(50) NOT NULL);

INSERT INTO configuration(key, value) VALUES ('matching.factor.waitingtime', 'true');
INSERT INTO configuration(key, value) VALUES ('matching.active', 'false');
INSERT INTO configuration(key, value) VALUES ('matching.minimum.disagreements.regular', '2');
INSERT INTO configuration(key, value) VALUES ('matching.minimum.disagreements.special', '1');

INSERT INTO configuration(key, value) VALUES ('current.special.active', 'true');
INSERT INTO configuration(key, value) VALUES ('current.number.questions.regular', '2');
INSERT INTO configuration(key, value) VALUES ('current.number.questions.special', '2');
INSERT INTO configuration(key, value) VALUES ('current.vote', 'someIdentifier');

CREATE TABLE questions(pkey serial PRIMARY KEY, identifier varchar(20) UNIQUE, questions varchar(200)[][] NOT NULL);

INSERT INTO questions(identifier, questions) VALUES ('regular', '{
	{"Eine Frage","Noch eine Frage"},
	{"Une question", "Encore une question"},
	{"A question","Another question"}
}');
INSERT INTO questions(identifier, questions) VALUES ('someIdentifier', '{
	{"Eine spezielle Frage","Noch eine spezielle Frage"},
	{"Une question spéciale", "Encore une question spéciale"},
	{"A special question","Another special question"}
}');
