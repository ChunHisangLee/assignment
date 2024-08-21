-- Drop users' table if exists
DROP TABLE IF EXISTS users CASCADE;

-- Create users' table
CREATE TABLE users
(
    id       SERIAL PRIMARY KEY,
    name     VARCHAR(255)        NOT NULL,
    email    VARCHAR(255) UNIQUE NOT NULL,
    password VARCHAR(255)        NOT NULL,
    CONSTRAINT email_unique UNIQUE (email)
);

-- Drop wallet table if exists
DROP TABLE IF EXISTS wallet CASCADE;

-- Create wallet table
CREATE TABLE wallet
(
    id          SERIAL PRIMARY KEY,
    usd_balance DECIMAL(18, 2) NOT NULL CHECK (usd_balance >= 0),
    btc_balance DECIMAL(18, 8) NOT NULL CHECK (btc_balance >= 0),
    user_id     INT            NOT NULL UNIQUE,
    CONSTRAINT fk_user FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE
);

-- Drop btc_price_history table if exists
DROP TABLE IF EXISTS btc_price_history CASCADE;

-- Create btc_price_history table
CREATE TABLE btc_price_history
(
    id        SERIAL PRIMARY KEY,
    price     DECIMAL(18, 2) NOT NULL CHECK (price >= 0),
    timestamp TIMESTAMP      NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Drop transactions table if exists
DROP TABLE IF EXISTS transactions CASCADE;

-- Create transactions table
CREATE TABLE transactions
(
    id                   SERIAL PRIMARY KEY,
    user_id              INT            NOT NULL,
    btc_price_history_id INT            NOT NULL,
    btc_amount           DECIMAL(18, 8) NOT NULL CHECK (btc_amount >= 0),
    transaction_time     TIMESTAMP      NOT NULL DEFAULT CURRENT_TIMESTAMP,
    transaction_type     VARCHAR(10)    NOT NULL CHECK (transaction_type IN ('BUY', 'SELL')),
    CONSTRAINT fk_user_transaction FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE,
    CONSTRAINT fk_btc_price_history FOREIGN KEY (btc_price_history_id) REFERENCES btc_price_history (id) ON DELETE CASCADE
);
