-- ============================================
-- 购买模块 + 订单模块 数据库设计
-- 基于现有表：user, site_list, collections, site, orders
-- ============================================

-- --------------------------------------------
-- 1. 门票表 (ticket)
-- 说明：每个景点可以有多种门票类型（成人票、儿童票、学生票等）
-- --------------------------------------------
CREATE TABLE IF NOT EXISTS ticket (
    id INT PRIMARY KEY AUTO_INCREMENT COMMENT '门票ID',
    site_id INT NOT NULL COMMENT '景点ID，关联site_list表',
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
-- 2. 购物车表 (cart) - 可选，如果需要购物车功能
-- 说明：用户暂存想购买的门票
-- --------------------------------------------
CREATE TABLE IF NOT EXISTS cart (
    id INT PRIMARY KEY AUTO_INCREMENT COMMENT '购物车ID',
    username VARCHAR(50) NOT NULL COMMENT '用户名，关联user表',
    ticket_id INT NOT NULL COMMENT '门票ID，关联ticket表',
    quantity INT NOT NULL DEFAULT 1 COMMENT '购买数量',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX idx_username (username),
    INDEX idx_ticket_id (ticket_id),
    FOREIGN KEY (username) REFERENCES user(username) ON DELETE CASCADE,
    FOREIGN KEY (ticket_id) REFERENCES ticket(id) ON DELETE CASCADE,
    UNIQUE KEY uk_user_ticket (username, ticket_id) COMMENT '同一用户同一门票只能有一条记录'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='购物车表';

-- --------------------------------------------
-- 3. 订单表 (orders)
-- 说明：如果已存在请检查字段，如果不存在则创建
-- 建议字段：订单号、用户名、订单状态、总金额、支付时间等
-- --------------------------------------------
-- 先删除旧表（如果存在且结构不符），或修改现有表
-- 这里提供完整的订单表结构，你可以根据实际情况调整

-- 如果orders表已存在，请使用ALTER TABLE修改
-- 如果不存在，使用以下SQL创建
CREATE TABLE IF NOT EXISTS orders (
    id INT PRIMARY KEY AUTO_INCREMENT COMMENT '订单ID',
    order_no VARCHAR(64) NOT NULL UNIQUE COMMENT '订单编号（唯一）',
    username VARCHAR(50) NOT NULL COMMENT '用户名，关联user表',
    total_amount DECIMAL(10, 2) NOT NULL COMMENT '订单总金额',
    pay_amount DECIMAL(10, 2) NOT NULL COMMENT '实际支付金额',
    status TINYINT DEFAULT 0 COMMENT '订单状态：0-待支付，1-已支付，2-已取消，3-已退款',
    pay_type TINYINT DEFAULT NULL COMMENT '支付方式：1-支付宝，2-微信，3-余额',
    pay_time DATETIME DEFAULT NULL COMMENT '支付时间',
    remark VARCHAR(500) DEFAULT NULL COMMENT '订单备注',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX idx_order_no (order_no),
    INDEX idx_username (username),
    INDEX idx_status (status),
    FOREIGN KEY (username) REFERENCES user(username) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='订单表';

-- --------------------------------------------
-- 4. 订单明细表 (order_item)
-- 说明：一个订单可以包含多个门票
-- --------------------------------------------
CREATE TABLE IF NOT EXISTS order_item (
    id INT PRIMARY KEY AUTO_INCREMENT COMMENT '明细ID',
    order_id INT NOT NULL COMMENT '订单ID，关联orders表',
    ticket_id INT NOT NULL COMMENT '门票ID，关联ticket表',
    ticket_name VARCHAR(100) NOT NULL COMMENT '门票名称（快照，防止门票信息修改后订单显示不一致）',
    ticket_price DECIMAL(10, 2) NOT NULL COMMENT '门票单价（快照）',
    quantity INT NOT NULL COMMENT '购买数量',
    subtotal DECIMAL(10, 2) NOT NULL COMMENT '小计金额',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    INDEX idx_order_id (order_id),
    INDEX idx_ticket_id (ticket_id),
    FOREIGN KEY (order_id) REFERENCES orders(id) ON DELETE CASCADE,
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
