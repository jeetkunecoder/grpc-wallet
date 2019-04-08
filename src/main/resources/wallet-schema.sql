DROP DATABASE IF EXISTS wallet;
CREATE DATABASE wallet;

DROP TABLE IF EXISTS balance;
DROP TABLE IF EXISTS currency;
DROP TABLE IF EXISTS user;

CREATE TABLE currency(
    id BIGINT NOT NULL PRIMARY KEY,
    code TEXT NOT NULL
);

INSERT INTO currency(id, code) VALUES (1, 'EUR');
INSERT INTO currency(id, code) VALUES (2, 'USD');
INSERT INTO currency(id, code) VALUES (3, 'GBP');

CREATE TABLE user(
    id BIGINT NOT NULL PRIMARY KEY
);

CREATE TABLE balance(
    id BIGINT NOT NULL PRIMARY KEY,
    user_id BIGINT NOT NULL,
    amount DECIMAL(13,2) NOT NULL,
    currency_id BIGINT NOT NULL,
    FOREIGN KEY (currency_id) REFERENCES currency(id)
);

