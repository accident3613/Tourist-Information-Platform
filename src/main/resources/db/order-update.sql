-- ============================================
-- 订单表修改 - 添加used和过期日期字段
-- ============================================

-- 添加used字段：0-未使用，1-已使用，2-已过期
ALTER TABLE orders ADD COLUMN used INT DEFAULT 0 COMMENT '使用状态：0-未使用，1-已使用，2-已过期';

-- 添加过期日期字段
ALTER TABLE orders ADD COLUMN expire_date DATETIME COMMENT '过期日期';

-- 为现有订单设置过期日期（创建后15天）
UPDATE orders SET expire_date = DATE_ADD(created_at, INTERVAL 15 DAY) WHERE expire_date IS NULL;

-- 添加索引
ALTER TABLE orders ADD INDEX idx_used (used);
ALTER TABLE orders ADD INDEX idx_expire_date (expire_date);
