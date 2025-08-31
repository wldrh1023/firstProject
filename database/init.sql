-- 데이터베이스 초기화 스크립트

-- 데이터베이스 생성 (이미 존재하는 경우 무시)
CREATE DATABASE IF NOT EXISTS gallery CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE gallery;

-- 사용자 권한 설정
GRANT ALL PRIVILEGES ON gallery.* TO 'root'@'%';
FLUSH PRIVILEGES;

-- 테이블 생성 (JPA가 자동으로 생성하지만, 수동으로도 생성 가능)
-- CREATE TABLE IF NOT EXISTS members (
--   id INT AUTO_INCREMENT PRIMARY KEY,
--   email VARCHAR(50) NOT NULL UNIQUE,
--   password VARCHAR(100) NOT NULL
-- );

-- CREATE TABLE IF NOT EXISTS items (
--   id INT AUTO_INCREMENT PRIMARY KEY,
--   name VARCHAR(100) NOT NULL,
--   price INT NOT NULL,
--   discount_per INT DEFAULT 0,
--   img_path VARCHAR(500)
-- );

-- CREATE TABLE IF NOT EXISTS orders (
--   id INT AUTO_INCREMENT PRIMARY KEY,
--   member_id INT NOT NULL,
--   name VARCHAR(50) NOT NULL,
--   address VARCHAR(500) NOT NULL,
--   payment VARCHAR(10) NOT NULL,
--   card_number VARCHAR(16),
--   items TEXT NOT NULL
-- );

-- CREATE TABLE IF NOT EXISTS carts (
--   id INT AUTO_INCREMENT PRIMARY KEY,
--   member_id INT NOT NULL,
--   item_id INT NOT NULL
-- );

