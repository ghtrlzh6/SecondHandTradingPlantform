-- Database schema for Campus BookSwap

-- Users table
CREATE TABLE IF NOT EXISTS users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL,
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
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (seller_id) REFERENCES users(id) ON DELETE CASCADE
);

-- Orders table (optional feature)
CREATE TABLE IF NOT EXISTS orders (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    book_id BIGINT NOT NULL,
    buyer_id BIGINT NOT NULL,
    ordered_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (book_id) REFERENCES books(id) ON DELETE CASCADE,
    FOREIGN KEY (buyer_id) REFERENCES users(id) ON DELETE CASCADE
);

-- Insert some sample data for testing
INSERT INTO users (username, password, email) VALUES 
('admin', 'admin123', 'admin@example.com'),
('alice', 'alice123', 'alice@example.com'),
('bob', 'bob123', 'bob@example.com');

INSERT INTO books (title, author, price, description, image_url, seller_id) VALUES 
('Java核心技术', 'Cay S. Horstmann', 89.00, 'Java经典教材', 'https://example.com/java-core.jpg', 1),
('Head First设计模式', 'Eric Freeman', 99.00, '设计模式入门经典', 'https://example.com/design-patterns.jpg', 2),
('算法导论', 'Thomas H. Cormen', 129.00, '算法权威指南', 'https://example.com/intro-algorithms.jpg', 1);