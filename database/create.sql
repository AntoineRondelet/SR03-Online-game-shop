-- If psql is up and running, run the following command in the terminal to create the database
-- and the following tables
-- $ createdb -p 2472 sr03 && psql -p 2472 -d sr03 -f ./create.sql
-- to insert the same way : psql -p 2472 -d sr03 -f ./create.sql -f ./insert.sql

BEGIN;

--DROP TYPE IF EXISTS PAYMENT_MODE CASCADE;
DROP TABLE IF EXISTS PAYMENT_MODE CASCADE;
CREATE TABLE PAYMENT_MODE (
    payment_mode TEXT PRIMARY KEY
);

--DROP TYPE IF EXISTS PAYMENT_STATUS CASCADE;
DROP TABLE IF EXISTS PAYMENT_STATUS CASCADE;
CREATE TABLE PAYMENT_STATUS (
    payment_status TEXT PRIMARY KEY
);

--DROP TABLE IF EXISTS CUSTOMER CASCADE;
DROP TABLE IF EXISTS CUSTOMER CASCADE;
CREATE TABLE CUSTOMER (
  username TEXT PRIMARY KEY NOT NULL,
  first_name TEXT NOT NULL,
  last_name TEXT NOT NULL,
  email TEXT NOT NULL,
  password TEXT NOT NULL,
  isActive BOOLEAN NOT NULL,
  birth_date DATE,
  subscription_date DATE NOT NULL,
  CHECK (birth_date <= subscription_date)
);

--we have to declare foreign key before creating a references
DROP TABLE IF EXISTS PURCHASE CASCADE;
CREATE TABLE PURCHASE (
  id SERIAL PRIMARY KEY,
  date_paid DATE,
  username TEXT NOT NULL,
  payment_mode TEXT,
  payment_status TEXT NOT NULL,
  FOREIGN KEY(username) REFERENCES CUSTOMER(username) ON DELETE CASCADE,
  FOREIGN KEY(payment_mode) REFERENCES PAYMENT_MODE(payment_mode) ON DELETE CASCADE,
  FOREIGN KEY(payment_status) REFERENCES PAYMENT_STATUS(payment_status) ON DELETE CASCADE
);

DROP TABLE IF EXISTS GAME_CONSOLE CASCADE;
CREATE TABLE GAME_CONSOLE (
  name TEXT PRIMARY KEY,
  release_date DATE,
  model TEXT,
  description TEXT,
  storage INTEGER
);

--we have to declare foreign key before creating a references
DROP TABLE IF EXISTS GAME CASCADE;
CREATE TABLE GAME (
  id SERIAL PRIMARY KEY,
  title TEXT NOT NULL,
  console TEXT NOT NULL,
  age_limit INTEGER NOT NULL,
  price FLOAT NOT NULL,
  release_date DATE NOT NULL,
  description TEXT,
  rate FLOAT,
  UNIQUE (title, console),
  FOREIGN KEY(console) REFERENCES GAME_CONSOLE(name) ON DELETE CASCADE
);

--we have to declare foreign key before creating a references
DROP TABLE IF EXISTS GAME_ENTITY CASCADE;
CREATE TABLE GAME_ENTITY (
    id TEXT PRIMARY KEY, -- The id corresponds to the serial number of the game
    id_game INTEGER NOT NULL,
    id_purchase INTEGER DEFAULT NULL,
    FOREIGN KEY (id_game) REFERENCES GAME(id) ON DELETE CASCADE
);

--we have to declare word as a primary key if we want to use it charaterize
--as a foreign key
DROP TABLE IF EXISTS GAME_KEYWORD CASCADE;
CREATE TABLE GAME_KEYWORD (
    word TEXT PRIMARY KEY
);

--we have to declare foreign key before creating a references
DROP TABLE IF EXISTS CHARACTERIZE CASCADE;
CREATE TABLE CHARACTERIZE (
  word TEXT,
  id_game INTEGER,
  FOREIGN KEY(word) REFERENCES GAME_KEYWORD(word) ON DELETE CASCADE,
  FOREIGN KEY(id_game) REFERENCES GAME(id) ON DELETE CASCADE,
  PRIMARY KEY (word, id_game)
);

END;
