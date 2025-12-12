-- Database schema for Campus BookSwap

-- Users table
CREATE TABLE IF NOT EXISTS users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL,
    rating DECIMAL(3,2) DEFAULT 5.00 COMMENT '用户评分，1.0-5.0',
    total_ratings INT DEFAULT 0 COMMENT '总评分次数',
    role VARCHAR(20) DEFAULT 'user' COMMENT '用户角色：user(普通用户), admin(管理员)',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Books table
CREATE TABLE IF NOT EXISTS books (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(200) NOT NULL,
    author VARCHAR(100) NOT NULL,
    price DECIMAL(10, 2) NOT NULL,
    description TEXT,
    image_url VARCHAR(500),
    seller_id BIGINT NOT NULL,
    status VARCHAR(20) DEFAULT 'available' COMMENT '书籍状态：available(可售), sold(已售), reserved(预留)',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (seller_id) REFERENCES users(id) ON DELETE CASCADE
);

-- Orders table (optional feature)
CREATE TABLE IF NOT EXISTS orders (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    book_id BIGINT NOT NULL,
    buyer_id BIGINT NOT NULL,
    shipping_address VARCHAR(500),
    payment_password VARCHAR(100),
    status VARCHAR(20) DEFAULT 'pending',
    ordered_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (book_id) REFERENCES books(id) ON DELETE CASCADE,
    FOREIGN KEY (buyer_id) REFERENCES users(id) ON DELETE CASCADE
);

-- Wallets table for user balances
CREATE TABLE IF NOT EXISTS wallets (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL UNIQUE,
    balance DECIMAL(10, 2) NOT NULL DEFAULT 0.00,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

-- Messages table for user communication
CREATE TABLE IF NOT EXISTS messages (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    sender_id BIGINT NOT NULL,
    receiver_id BIGINT NOT NULL,
    book_id BIGINT NOT NULL COMMENT '书籍ID，负数表示申诉消息（绝对值为订单ID）',
    content TEXT NOT NULL,
    sent_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    is_read BOOLEAN DEFAULT FALSE,
    FOREIGN KEY (sender_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (receiver_id) REFERENCES users(id) ON DELETE CASCADE
    -- 移除book_id的外键约束以支持申诉消息（负数book_id）
);

-- Insert some sample data for testing
INSERT INTO users (username, password, email, rating, total_ratings, role) VALUES 
('admin', 'admin123', 'admin@example.com', 4.8, 25, 'admin'),
('alice', 'alice123', 'alice@example.com', 4.5, 12, 'user'),
('bob', 'bob123', 'bob@example.com', 4.2, 8, 'user');

INSERT INTO books (title, author, price, description, image_url, seller_id) VALUES 
('Core Java', 'Cay S. Horstmann', 89.00, 'Classic Java Textbook', 'https://example.com/java-core.jpg', 1),
('Head First Design Patterns', 'Eric Freeman', 99.00, 'Introduction to Design Patterns: A Classic Guide', 'https://example.com/design-patterns.jpg', 2),
('Introduction to Algorithms', 'Thomas H. Cormen', 129.00, 'Authoritative Guide to Algorithms', 'https://example.com/intro-algorithms.jpg', 1);
