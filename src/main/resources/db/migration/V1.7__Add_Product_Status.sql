-- V1.7__Add_Product_Status.sql

ALTER TABLE products
ADD COLUMN status VARCHAR(20) NOT NULL DEFAULT 'DISABLED';
