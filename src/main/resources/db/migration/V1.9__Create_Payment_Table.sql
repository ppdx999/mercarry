-- V1.9__Create_Payment_Table.sql

CREATE TABLE payments (
		id BIGSERIAL PRIMARY KEY,
		product_id BIGINT NOT NULL REFERENCES products(id),
		order_id BIGINT NOT NULL REFERENCES orders(id),
		receiver_id BIGINT NOT NULL REFERENCES users(id),
		sender_id BIGINT NOT NULL REFERENCES users(id),
		amount DECIMAL(10, 2) NOT NULL,
		status VARCHAR(20) NOT NULL,
		created_at TIMESTAMP NOT NULL DEFAULT NOW()
);
