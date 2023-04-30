CREATE TABLE IF NOT EXISTS _user (
    id SERIAL PRIMARY KEY,
    firstname VARCHAR(50),
    lastname VARCHAR(50),
    email VARCHAR(50),
    password VARCHAR(100),
    role VARCHAR(50)
    );