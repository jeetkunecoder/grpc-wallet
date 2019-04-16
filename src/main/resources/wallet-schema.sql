DROP TABLE IF EXISTS balance;
DROP TABLE IF EXISTS currency;
DROP TABLE IF EXISTS system_user;

CREATE TABLE currency(
    id BIGINT NOT NULL PRIMARY KEY,
    code TEXT NOT NULL
);

INSERT INTO currency(id, code) VALUES (0, 'INVALID');
INSERT INTO currency(id, code) VALUES (1, 'EUR');
INSERT INTO currency(id, code) VALUES (2, 'USD');
INSERT INTO currency(id, code) VALUES (3, 'GBP');

CREATE TABLE system_user(
    id BIGINT NOT NULL PRIMARY KEY,
    name TEXT NOT NULL
);

CREATE TABLE balance(
    id BIGINT NOT NULL PRIMARY KEY,
    user_id BIGINT NOT NULL,
    FOREIGN KEY (user_id) REFERENCES system_user(id),
    amount DECIMAL(13,2) NOT NULL,
    currency_id BIGINT NOT NULL,
    FOREIGN KEY (currency_id) REFERENCES currency(id),
    created_at TIMESTAMP,
    operation_type SMALLINT NOT NULL -- 0: transaction, 1: summary
);


