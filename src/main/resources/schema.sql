DROP TABLE IF EXISTS users;

CREATE TABLE users
(
   id serial primary key,
   name VARCHAR(100) NOT NULL UNIQUE,
   password VARCHAR(100) NOT NULL,
   authority VARCHAR(100)
);