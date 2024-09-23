-- V1.4__Create_Product_Image_Table.sql

CREATE TABLE product_images (
    id BIGSERIAL PRIMARY KEY,
    product_id BIGINT NOT NULL REFERENCES products(id) ON DELETE CASCADE,
    content_id VARCHAR(255) NOT NULL,
    content_length BIGINT NOT NULL,
    mime_type VARCHAR(127) NOT NULL
);

alter table products add column top_image_id bigint references product_images(id) on delete set null;