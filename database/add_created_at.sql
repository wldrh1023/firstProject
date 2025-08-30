-- orders 테이블에 createdAt 컬럼 추가
USE gallery;

-- 현재 테이블 구조 확인
DESCRIBE orders;

-- createdAt 컬럼이 없다면 추가
ALTER TABLE orders 
ADD COLUMN IF NOT EXISTS created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
ADD COLUMN IF NOT EXISTS updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP;

-- 기존 데이터에 대해 createdAt을 현재 시간으로 설정 (NULL인 경우)
UPDATE orders SET created_at = CURRENT_TIMESTAMP WHERE created_at IS NULL;

-- 테이블 구조 다시 확인
DESCRIBE orders;

-- 샘플 데이터 확인
SELECT id, name, created_at, updated_at FROM orders LIMIT 5;
