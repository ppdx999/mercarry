-- V1.5__Create_Wallet_Table.sql

CREATE TABLE wallets (
    id BIGSERIAL PRIMARY KEY,
    balance DECIMAL(10, 2) NOT NULL,
    user_id BIGINT NOT NULL REFERENCES users(id)
);