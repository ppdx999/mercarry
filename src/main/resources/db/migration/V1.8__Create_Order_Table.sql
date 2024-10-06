-- V1.8__Create_Order_Table.sql

CREATE TABLE orders (
		id BIGSERIAL PRIMARY KEY,
		-- product_is is unique to prevent multiple orders for the same product
		product_id BIGINT NOT NULL REFERENCES products(id) UNIQUE,
		buyer_id BIGINT NOT NULL REFERENCES users(id),
		seller_id BIGINT NOT NULL REFERENCES users(id),
		price DECIMAL(10, 2) NOT NULL,
		created_at TIMESTAMP NOT NULL DEFAULT NOW()
);
