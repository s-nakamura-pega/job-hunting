-- ============================
-- customers（顧客マスタ）
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
('株式会社サンプル', '東京都品川区1-2-3', '03-1111-2222', datetime('now')),
('テスト商事株式会社', '東京都大田区4-5-6', '03-3333-4444', datetime('now')),
('デモ販売株式会社', '東京都港区7-8-9', '03-5555-6666', datetime('now'));



-- ============================
-- orders（受注ヘッダ）
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
(1, date('now'), 15000, '受付'),
(2, date('now'), 32000, '出荷済'),
(1, date('now'), 8000, '受付');



-- ============================
-- order_items（受注明細）
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
(1, 'ノートPC', 1, 15000),
(2, 'モニター', 2, 12000),
(2, 'HDMIケーブル', 1, 8000),
(3, 'マウス', 2, 4000);
