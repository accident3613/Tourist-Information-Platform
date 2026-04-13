-- ============================================
-- 管理端数据库设计
-- ============================================

-- --------------------------------------------
-- 1. 管理员表 (admin)
-- --------------------------------------------
CREATE TABLE IF NOT EXISTS admin (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '管理员ID',
    username VARCHAR(50) NOT NULL UNIQUE COMMENT '管理员账号',
    password VARCHAR(255) NOT NULL COMMENT '密码(BCrypt加密)',
    name VARCHAR(50) COMMENT '管理员姓名',
    role ENUM('super', 'admin', 'operator') DEFAULT 'operator' COMMENT '角色：super-超级管理员, admin-管理员, operator-运营人员',
    status TINYINT DEFAULT 1 COMMENT '状态：0-禁用 1-启用',
    last_login_time DATETIME COMMENT '最后登录时间',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX idx_username (username),
    INDEX idx_role (role),
    INDEX idx_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='管理员表';

-- 插入默认超级管理员 (密码: admin123)
INSERT INTO admin (username, password, name, role, status) VALUES
('admin', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iAt6Z5EO', '超级管理员', 'super', 1);

-- --------------------------------------------
-- 2. 管理员操作日志表 (admin_log)
-- --------------------------------------------
CREATE TABLE IF NOT EXISTS admin_log (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '日志ID',
    admin_id BIGINT COMMENT '管理员ID',
    admin_username VARCHAR(50) COMMENT '管理员账号',
    action VARCHAR(100) COMMENT '操作类型',
    target_type VARCHAR(50) COMMENT '操作对象类型(user/site/ticket/order等)',
    target_id VARCHAR(100) COMMENT '操作对象ID',
    detail TEXT COMMENT '操作详情',
    ip VARCHAR(50) COMMENT 'IP地址',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    INDEX idx_admin_id (admin_id),
    INDEX idx_action (action),
    INDEX idx_target_type (target_type),
    INDEX idx_create_time (create_time)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='管理员操作日志表';
