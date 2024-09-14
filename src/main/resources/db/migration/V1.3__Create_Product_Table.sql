-- V1.3__Create_Product_Table.sql

CREATE TABLE products (
		id BIGSERIAL PRIMARY KEY,
		name VARCHAR(100) NOT NULL,
		price DECIMAL(10, 2) NOT NULL,
		supplier_id BIGINT NOT NULL REFERENCES users(id)
);
