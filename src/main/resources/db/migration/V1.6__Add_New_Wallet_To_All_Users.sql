-- V1.6__Add_New_Wallet_To_All_Users.sql
INSERT INTO wallets (balance, user_id) SELECT 0, id FROM users;