-- Create orders table
CREATE TABLE IF NOT EXISTS t_orders (
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    order_number VARCHAR(255) NOT NULL,
    sku_code    VARCHAR(255) NOT NULL,
    price       DECIMAL(10,2) NOT NULL,
    quantity    INT NOT NULL,
    created_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
