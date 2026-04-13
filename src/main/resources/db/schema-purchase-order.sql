-- ============================================
-- 购买模块 + 订单模块 数据库设计
-- 基于现有表结构适配
-- ============================================

-- --------------------------------------------
-- 1. 门票表 (ticket)
-- 说明：每个景点可以有多种门票类型（成人票、儿童票、学生票等）
-- 外键：site_id 关联 site_list.id (bigint)
-- --------------------------------------------
CREATE TABLE IF NOT EXISTS ticket (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '门票ID',
    site_id BIGINT NOT NULL COMMENT '景点ID，关联site_list表',
    name VARCHAR(100) NOT NULL COMMENT '门票名称（如：成人票、学生票）',
    price DECIMAL(10, 2) NOT NULL COMMENT '门票价格',
    original_price DECIMAL(10, 2) DEFAULT NULL COMMENT '原价（用于显示折扣）',
    stock INT DEFAULT 0 COMMENT '库存数量，-1表示不限量',
    description TEXT COMMENT '门票说明',
    status TINYINT DEFAULT 1 COMMENT '状态：0-下架，1-上架',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX idx_site_id (site_id),
    INDEX idx_status (status),
    FOREIGN KEY (site_id) REFERENCES site_list(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='门票表';

-- --------------------------------------------
-- 2. 订单明细表 (order_item)
-- 说明：一个订单可以包含多个门票
-- 外键：order_id 关联 orders.order_id (varchar)
-- --------------------------------------------
CREATE TABLE IF NOT EXISTS order_item (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '明细ID',
    order_id VARCHAR(50) NOT NULL COMMENT '订单ID，关联orders表的order_id字段',
    ticket_id BIGINT NOT NULL COMMENT '门票ID，关联ticket表',
    ticket_name VARCHAR(100) NOT NULL COMMENT '门票名称（快照）',
    ticket_price DECIMAL(10, 2) NOT NULL COMMENT '门票单价（快照）',
    quantity INT NOT NULL COMMENT '购买数量',
    subtotal DECIMAL(10, 2) NOT NULL COMMENT '小计金额',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    INDEX idx_order_id (order_id),
    INDEX idx_ticket_id (ticket_id),
    FOREIGN KEY (ticket_id) REFERENCES ticket(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='订单明细表';

-- --------------------------------------------
-- 初始化数据示例（可选）
-- --------------------------------------------

-- 为现有景点添加门票示例（请根据实际情况修改site_id）
-- INSERT INTO ticket (site_id, name, price, original_price, stock, description) VALUES
-- (1, '成人票', 120.00, 150.00, 1000, '适用于18-60岁成人'),
-- (1, '学生票', 60.00, 150.00, 500, '需出示有效学生证'),
-- (1, '儿童票', 30.00, 150.00, 500, '适用于1.2米以下儿童'),
-- (2, '成人票', 80.00, 100.00, 800, '适用于18-60岁成人'),
-- (2, '老人票', 40.00, 100.00, 300, '适用于60岁以上老人');
