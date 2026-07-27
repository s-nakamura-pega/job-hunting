-- ============================
-- customers (Customer Master)
-- ============================
DROP TABLE IF EXISTS customers;

CREATE TABLE customers (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    name TEXT NOT NULL,
    address TEXT,
    phone TEXT,
    created_at TEXT NOT NULL
);

INSERT INTO customers (name, address, phone, created_at) VALUES
('Sample Corporation', '1-2-3 Shinagawa-ku, Tokyo', '03-1111-2222', datetime('now')),
('Test Trading Co., Ltd.', '4-5-6 Ota-ku, Tokyo', '03-3333-4444', datetime('now')),
('Demo Sales Co., Ltd.', '7-8-9 Minato-ku, Tokyo', '03-5555-6666', datetime('now'));



-- ============================
-- orders (Order Header)
-- ============================
DROP TABLE IF EXISTS orders;

CREATE TABLE orders (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    customer_id INTEGER NOT NULL,
    order_date TEXT NOT NULL,
    total_amount REAL NOT NULL,
    status TEXT NOT NULL,
    FOREIGN KEY (customer_id) REFERENCES customers(id)
);

INSERT INTO orders (customer_id, order_date, total_amount, status) VALUES
(1, date('now'), 15000, 'Received'),
(2, date('now'), 32000, 'Shipped'),
(1, date('now'), 8000, 'Received');



-- ============================
-- order_items (Order Details)
-- ============================
DROP TABLE IF EXISTS order_items;

CREATE TABLE order_items (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    order_id INTEGER NOT NULL,
    product_name TEXT NOT NULL,
    quantity INTEGER NOT NULL,
    price REAL NOT NULL,
    FOREIGN KEY (order_id) REFERENCES orders(id)
);

INSERT INTO order_items (order_id, product_name, quantity, price) VALUES
(1, 'Laptop', 1, 15000),
(2, 'Monitor', 2, 12000),
(2, 'HDMI Cable', 1, 8000),
(3, 'Mouse', 2, 4000);
