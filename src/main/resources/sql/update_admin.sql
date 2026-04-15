-- 修改admin表，添加site_id字段
ALTER TABLE admin ADD COLUMN site_id BIGINT DEFAULT NULL COMMENT '所属景点ID（仅运营人员使用）' AFTER role;

-- 添加索引
ALTER TABLE admin ADD INDEX idx_site_id (site_id);

-- 更新现有运营人员数据（如果有的话）
-- UPDATE admin SET site_id = 1 WHERE role = 'operator';
