-- V1.10_ALTER_Product_Table_supplier_to_seller.sql

ALTER TABLE products
RENAME COLUMN supplier_id TO seller_id;
