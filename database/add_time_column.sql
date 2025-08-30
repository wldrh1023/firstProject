-- 모든 테이블에 time 컬럼 추가
USE gallery;

-- orders 테이블에 time 컬럼 추가
ALTER TABLE orders ADD COLUMN time TIMESTAMP DEFAULT CURRENT_TIMESTAMP;

-- members 테이블에 time 컬럼 추가
ALTER TABLE members ADD COLUMN time TIMESTAMP DEFAULT CURRENT_TIMESTAMP;

-- items 테이블에 time 컬럼 추가
ALTER TABLE items ADD COLUMN time TIMESTAMP DEFAULT CURRENT_TIMESTAMP;

-- carts 테이블에 time 컬럼 추가
ALTER TABLE carts ADD COLUMN time TIMESTAMP DEFAULT CURRENT_TIMESTAMP;



-- 테이블 구조 확인
DESCRIBE orders;
DESCRIBE members;
DESCRIBE items;
DESCRIBE carts;

