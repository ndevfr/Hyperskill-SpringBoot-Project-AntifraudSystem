/* DROP TABLE IF EXISTS users_details;
DROP TABLE IF EXISTS users;
DROP TABLE IF EXISTS suspisicous_ip;
DROP TABLE IF EXISTS stolen_cards;
DROP TABLE IF EXISTS transactions; */

CREATE TABLE IF NOT EXISTS users (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name VARCHAR(50) NOT NULL
);

CREATE TABLE IF NOT EXISTS users_details (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    user_id BIGINT NOT NULL,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(80) NOT NULL,
    role VARCHAR(20) NOT NULL DEFAULT 'MERCHANT',
    unlocked BOOLEAN NOT NULL DEFAULT FALSE,
    CONSTRAINT fk_users FOREIGN KEY (user_id) REFERENCES users(id)
);

CREATE TABLE IF NOT EXISTS suspicious_ip (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    ip VARCHAR(15) NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS stolen_cards (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    number VARCHAR(16) NOT NULL UNIQUE
);


CREATE TABLE IF NOT EXISTS transactions (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    amount BIGINT NOT NULL,
    ip VARCHAR(15) NOT NULL,
    number VARCHAR(16) NOT NULL,
    region VARCHAR(10) NOT NULL,
    date TIMESTAMP NOT NULL,
    type VARCHAR(20)
);

CREATE TABLE IF NOT EXISTS feedbacks (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    message VARCHAR(25) NOT NULL DEFAULT '',
    transaction_id BIGINT NOT NULL,
    CONSTRAINT fk_transaction FOREIGN KEY(transaction_id) REFERENCES transactions(id)
);

CREATE TABLE IF NOT EXISTS card_limits (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    number VARCHAR(16) NOT NULL UNIQUE,
    allowed BIGINT NOT NULL DEFAULT 200,
    manual BIGINT NOT NULL DEFAULT 1500
);