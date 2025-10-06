-- Create products table with JSONB support for variants
CREATE TABLE products (
    id BIGSERIAL PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    handle VARCHAR(255),
    vendor VARCHAR(255),
    product_type VARCHAR(255),
    price DECIMAL(10,2),
    compare_at_price DECIMAL(10,2),
    sku VARCHAR(255),
    available BOOLEAN,
    description TEXT,
    image_url VARCHAR(500),
    variants JSONB,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Create indexes for better performance
CREATE INDEX idx_products_title ON products(title);
CREATE INDEX idx_products_vendor ON products(vendor);
CREATE INDEX idx_products_sku ON products(sku);
CREATE INDEX idx_products_available ON products(available);
CREATE INDEX idx_products_variants ON products USING GIN (variants);

