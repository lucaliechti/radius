--cantons
DROP TABLE IF EXISTS cantons CASCADE;

CREATE TABLE cantons (
    pkey int PRIMARY KEY,
    code varchar(2) UNIQUE
);

INSERT INTO cantons (code, pkey) VALUES
('AG',19),
('AI',15),
('AR',16),
('BE',2),
('BL',13),
('BS',12),
('FR',10),
('GE',25),
('GL',8),
('GR',18),
('JU',26),
('LU',3),
('NE',24),
('NW',7),
('OW',6),
('SG',17),
('SH',14),
('SO',11),
('SZ',5),
('TG',20),
('TI',21),
('UR',4),
('VD',22),
('VS',23),
('ZG',9),
('ZH',1);

--users
DROP TABLE IF EXISTS users CASCADE;

CREATE TABLE users (
	pkey serial PRIMARY KEY,
	status varchar(20) NOT NULL,
	firstname varchar(100) NOT NULL,
	lastname varchar(100) NOT NULL,
	email varchar(100) UNIQUE NOT NULL,
	password varchar(100) NOT NULL,
	canton varchar(2) REFERENCES cantons (code),
	modus varchar(10),
	languages varchar(20),
	locations varchar(1000),
	enabled boolean NOT NULL,
	answered boolean NOT NULL,
	motivation varchar(8000),
	q1 boolean,
	q2 boolean,
	q3 boolean,
	q4 boolean,
	q5 boolean,
	banned boolean NOT NULL,
	uuid varchar(100),
	datecreate timestamp with time zone,
    datemodify timestamp with time zone
);

--INSERT INTO users (status, firstname, lastname, email, password, enabled, answered, banned) VALUES ('WAITING', 'Luca', 'Liechti', 'email@example.com', 'verysecret', 'true', 'false', 'false');
--DELETE FROM users;

--authorities
DROP TABLE IF EXISTS authorities CASCADE;

CREATE TABLE authorities (
    pkey serial PRIMARY KEY,
    email varchar(100) REFERENCES users (email),
    authority varchar(20),
    datecreate timestamp with time zone,
    datemodify timestamp with time zone
);

--experiences
DROP TABLE IF EXISTS experiences CASCADE;

CREATE TABLE experiences (
    pkey serial PRIMARY KEY,
    experience varchar(8000),
    user_email varchar(100) REFERENCES users (email),
    datecreate timestamp with time zone,
    datemodify timestamp with time zone
);

DROP TABLE IF EXISTS matches CASCADE;

CREATE TABLE matches(
	pkey serial primary key,
	datecreated timestamp with time zone NOT NULL,
	email1 varchar(255) NOT NULL REFERENCES users (email),
	email2 varchar(255) NOT NULL REFERENCES users (email),
	active boolean NOT NULL,
	meetingconfirmed boolean,
	dateinactive timestamp with time zone
);

--regions
DROP TABLE IF EXISTS regions CASCADE;

CREATE TABLE regions (
    pkey int PRIMARY KEY,
    region varchar(50) UNIQUE
);

INSERT INTO regions (pkey, region) VALUES
(1, 'Zürich'),
(2, 'Glattal/Furttal'),
(3, 'Limmattal'),
(4, 'Knonaueramt'),
(5, 'Zimmerberg'),
(6, 'Pfannenstiel'),
(7, 'Zürcher Oberland'),
(8, 'Winterthur'),
(9, 'Weinland'),
(10, 'Zürcher Unterland'),
(11, 'Bern'),
(12, 'Erlach/Seeland'),
(13, 'Biel/Seeland'),
(14, 'Jura bernois'),
(15, 'Oberaargau'),
(16, 'Burgdorf'),
(17, 'Oberes Emmental'),
(18, 'Aaretal'),
(19, 'Schwarzwasser'),
(20, 'Thun/Innertport'),
(21, 'Saanen/Oberes Simmental'),
(22, 'Kandertal'),
(23, 'Oberland-Ost'),
(24, 'Grenchen'),
(25, 'Laufental'),
(26, 'Luzern'),
(27, 'Sursee/Seetal'),
(28, 'Willisau'),
(29, 'Entlebuch'),
(30, 'Uri'),
(31, 'Innerschwyz'),
(32, 'Einsiedeln'),
(33, 'March'),
(34, 'Sarneraatal'),
(35, 'Nidwalden/Engelberg'),
(36, 'Glarner Mittel-/Unterland'),
(37, 'Glarner Hinterland'),
(38, 'Zug'),
(39, 'La Sarine'),
(40, 'La Gruyère'),
(41, 'Sense'),
(42, 'Murten'),
(43, 'Glâne/Veveyse'),
(44, 'Olten/Gösgen/Gäu'),
(45, 'Thal'),
(46, 'Solothurn'),
(47, 'Basel-Stadt'),
(48, 'Unteres Baselbiet'),
(49, 'Oberes Baselbiet'),
(50, 'Schaffhausen'),
(51, 'Appenzell Ausserrhoden'),
(52, 'Appenzell Innerrhoden'),
(53, 'St. Gallen/Rorschach'),
(54, 'Rheintal SG'),
(55, 'Werdenberg'),
(56, 'Sarganserland'),
(57, 'Linthgebiet'),
(58, 'Toggenburg'),
(59, 'Wil'),
(60, 'Bündner Rheintal'),
(61, 'Prättigau'),
(62, 'Davos'),
(63, 'Schanfigg'),
(64, 'Mittelbünden'),
(65, 'Domleschg/Hinterrhein'),
(66, 'Surselva'),
(67, 'Engiadina bassa'),
(68, 'Oberengadin'),
(69, 'Mesolcina'),
(70, 'Aarau'),
(71, 'Brugg/Zurzach'),
(72, 'Baden'),
(73, 'Rohrdorf/Mutschellen'),
(74, 'Freiamt'),
(75, 'Fricktal'),
(76, 'Thurtal'),
(77, 'Untersee/Rhein'),
(78, 'Oberthurgau'),
(79, 'Tre Valli'),
(80, 'Locarno'),
(81, 'Bellinzona'),
(82, 'Lugano'),
(83, 'Mendrisio'),
(84, 'Lausanne'),
(85, 'Morges/Rolle'),
(86, 'Nyon'),
(87, 'Vevey/Lavaux'),
(88, 'Aigle'),
(89, 'Pays d''Enhaut'),
(90, 'Gros-de-Vaud'),
(91, 'Yverdon'),
(92, 'La Vallée'),
(93, 'La Broye'),
(94, 'Goms'),
(95, 'Brig-Östliches Raron'),
(96, 'Visp-Westliches Raron'),
(97, 'Leuk'),
(98, 'Sierre'),
(99, 'Sion'),
(100, 'Martigny'),
(101, 'Monthey/St-Maurice'),
(102, 'Neuchâtel'),
(103, 'La Chaux-de-Fonds'),
(104, 'Val-de-Travers'),
(105, 'Genève'),
(106, 'Jura');

--email confirmations
DROP TABLE IF EXISTS uuids CASCADE;

CREATE TABLE uuids (
    pkey serial PRIMARY KEY,
    email varchar(100) REFERENCES users (email),
    uuid varchar(100) NOT NULL
);

--newsletter
DROP TABLE IF EXISTS newsletter CASCADE;

CREATE TABLE newsletter (
    pkey serial PRIMARY KEY,
    datecreate timestamp with time zone NOT NULL,
    email varchar(100),
    source varchar(30),
    uuid varchar(50)
);

--survey
DROP TABLE IF EXISTS survey CASCADE;

CREATE TABLE survey (
	pkey serial PRIMARY KEY,
	datecreate timestamp with time zone NOT NULL,
	questions varchar(50),
	answers varchar(100),
	newsletter boolean NOT NULL,
	registration boolean NOT NULL
);