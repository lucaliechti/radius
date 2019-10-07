CREATE TABLE votes(
    pkey serial PRIMARY KEY,
    email varchar(100) NOT NULL REFERENCES users (email),
    votenr varchar(10),
    answer varchar(100)
);