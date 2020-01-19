CREATE TABLE pressreleases (
    pkey serial PRIMARY KEY,
    releasedate date NOT NULL,
    links varchar(200)[] NOT NULL
);

CREATE TABLE news (
      pkey serial PRIMARY KEY,
      newsdate date NOT NULL,
      titles varchar(200)[] NOT NULL,
      texts varchar(8000)[] NOT NULL
);
