-- 评论表
CREATE TABLE IF NOT EXISTS comment (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '评论ID',
    site_id BIGINT NOT NULL COMMENT '景点ID',
    username VARCHAR(100) NOT NULL COMMENT '用户名',
    content TEXT NOT NULL COMMENT '评论内容',
    rating INT DEFAULT 5 COMMENT '评分(1-5)',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    INDEX idx_site_id (site_id),
    INDEX idx_username (username),
    INDEX idx_create_time (create_time)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='景点评论表';
