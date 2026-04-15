-- 农产品表
CREATE TABLE IF NOT EXISTS agri_product (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '农产品ID',
    site_id BIGINT NOT NULL COMMENT '所属景点ID',
    name VARCHAR(100) NOT NULL COMMENT '农产品名称',
    description TEXT COMMENT '产品描述',
    price_per_jin DECIMAL(10,2) NOT NULL COMMENT '每斤价格',
    stock INT DEFAULT 0 COMMENT '库存（斤）',
    status TINYINT DEFAULT 1 COMMENT '状态：1上架 0下架',
    rating DECIMAL(3,2) DEFAULT 5.00 COMMENT '评分',
    sales_count INT DEFAULT 0 COMMENT '销量',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX idx_site_id (site_id),
    INDEX idx_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='农产品表';

-- 农产品图片表
CREATE TABLE IF NOT EXISTS agri_image (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '图片ID',
    product_id BIGINT NOT NULL COMMENT '农产品ID',
    image_path VARCHAR(255) NOT NULL COMMENT '图片路径',
    is_main TINYINT DEFAULT 0 COMMENT '是否主图：1是 0否',
    sort_order INT DEFAULT 0 COMMENT '排序',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    INDEX idx_product_id (product_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='农产品图片表';

-- 农产品评论表
CREATE TABLE IF NOT EXISTS agri_comment (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '评论ID',
    product_id BIGINT NOT NULL COMMENT '农产品ID',
    username VARCHAR(100) NOT NULL COMMENT '用户名',
    content TEXT NOT NULL COMMENT '评论内容',
    rating INT DEFAULT 5 COMMENT '评分(1-5)',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    INDEX idx_product_id (product_id),
    INDEX idx_username (username)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='农产品评论表';

-- 农产品订单表
CREATE TABLE IF NOT EXISTS agri_order (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '订单ID',
    order_no VARCHAR(50) NOT NULL COMMENT '订单编号',
    username VARCHAR(100) NOT NULL COMMENT '用户名',
    product_id BIGINT NOT NULL COMMENT '农产品ID',
    product_name VARCHAR(100) NOT NULL COMMENT '产品名称',
    quantity_jin INT NOT NULL COMMENT '购买斤数',
    price_per_jin DECIMAL(10,2) NOT NULL COMMENT '单价',
    total_price DECIMAL(10,2) NOT NULL COMMENT '总价',
    status VARCHAR(20) DEFAULT 'pending' COMMENT '状态：pending待支付 confirmed已确认 completed已完成 cancelled已取消',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    pay_time DATETIME COMMENT '支付时间',
    INDEX idx_username (username),
    INDEX idx_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='农产品订单表';

-- ============================================
-- 清空旧数据（如果需要重新初始化）
-- ============================================
-- TRUNCATE TABLE agri_comment;
-- TRUNCATE TABLE agri_image;
-- TRUNCATE TABLE agri_order;
-- TRUNCATE TABLE agri_product;

-- ============================================
-- 插入农产品数据（对应8个景点，每个景点3个产品）
-- ============================================

-- 昭阳公园 (site_id=1) - 湖南邵东特色
INSERT INTO agri_product (site_id, name, description, price_per_jin, stock, status, rating, sales_count) VALUES
(1, '邵东黄花菜', '邵东特产黄花菜，色泽金黄，口感鲜嫩，营养丰富。', 35.00, 200, 1, 4.7, 80),
(1, '昭阳湖莲子', '昭阳湖莲藕所产莲子，颗粒饱满，清心安神。', 42.00, 150, 1, 4.6, 60),
(1, '邵东茶油', '本地山茶籽压榨，纯天然无添加，营养丰富。', 68.00, 100, 1, 4.8, 45);

-- 大云山 (site_id=2) - 山地特色
INSERT INTO agri_product (site_id, name, description, price_per_jin, stock, status, rating, sales_count) VALUES
(2, '云山云雾茶', '大云山高山云雾茶，清香持久，回甘悠长。', 128.00, 80, 1, 4.8, 35),
(2, '野生山茶菇', '大云山野生菌菇，肉质厚实，香味浓郁。', 58.00, 120, 1, 4.6, 50),
(2, '山竹笋干', '大云山春笋晒干，鲜嫩可口，煲汤佳品。', 38.00, 200, 1, 4.5, 70);

-- 丽岭潭 (site_id=3) - 水域特色
INSERT INTO agri_product (site_id, name, description, price_per_jin, stock, status, rating, sales_count) VALUES
(3, '丽岭野生鱼干', '丽岭潭野生鱼晒干，肉质紧实，香味独特。', 78.00, 60, 1, 4.7, 40),
(3, '潭边水芹菜', '丽岭潭边种植的水芹菜，清香脆嫩，无污染。', 15.00, 300, 1, 4.5, 90),
(3, '莲子藕粉', '丽岭潭莲藕精制藕粉，细腻爽滑，营养丰富。', 48.00, 150, 1, 4.6, 55);

-- 太阳岛 (site_id=4) - 岛屿特色
INSERT INTO agri_product (site_id, name, description, price_per_jin, stock, status, rating, sales_count) VALUES
(4, '太阳岛柑橘', '太阳岛阳光充足，柑橘甜度高，汁多味美。', 12.00, 500, 1, 4.8, 150),
(4, '岛产红薯干', '太阳岛沙地红薯制作，软糯香甜，天然健康。', 22.00, 300, 1, 4.6, 80),
(4, '太阳岛蜂蜜', '岛上百花蜜，纯天然采集，香甜浓郁。', 88.00, 80, 1, 4.7, 35);

-- 佘湖山景区 (site_id=5) - 山地特色
INSERT INTO agri_product (site_id, name, description, price_per_jin, stock, status, rating, sales_count) VALUES
(5, '佘湖山毛尖', '佘湖山绿茶，芽叶细嫩，清香持久。', 108.00, 100, 1, 4.7, 42),
(5, '野生板栗', '佘湖山野生板栗，香甜软糯，营养丰富。', 28.00, 250, 1, 4.6, 65),
(5, '山核桃', '佘湖山山核桃，壳薄仁满，香脆可口。', 45.00, 180, 1, 4.5, 55);

-- 乌鸡岭景区 (site_id=6) - 山区特色
INSERT INTO agri_product (site_id, name, description, price_per_jin, stock, status, rating, sales_count) VALUES
(6, '乌鸡岭土鸡', '本地散养土鸡，肉质紧实，汤鲜味美。', 38.00, 80, 1, 4.8, 60),
(6, '土鸡蛋', '散养土鸡蛋，蛋黄饱满，营养丰富。', 18.00, 200, 1, 4.7, 100),
(6, '野生葛根粉', '乌鸡岭野生葛根制作，清热解毒，美容养颜。', 55.00, 120, 1, 4.6, 45);

-- 大金旅游度假村 (site_id=7) - 度假村特色
INSERT INTO agri_product (site_id, name, description, price_per_jin, stock, status, rating, sales_count) VALUES
(7, '大金有机大米', '度假村有机种植，米粒饱满，香软可口。', 15.00, 400, 1, 4.8, 120),
(7, '农家腊肉', '传统手工腌制，烟熏香味浓郁，肥而不腻。', 68.00, 100, 1, 4.7, 85),
(7, '度假村蔬菜干', '有机蔬菜晒干，绿色健康，煲汤炒菜皆宜。', 25.00, 200, 1, 4.5, 50);

-- 小凯龙虾池 (site_id=8) - 水产特色
INSERT INTO agri_product (site_id, name, description, price_per_jin, stock, status, rating, sales_count) VALUES
(8, '小凯小龙虾', '本地养殖小龙虾，肉质鲜嫩，壳薄肉多。', 35.00, 150, 1, 4.9, 200),
(8, '龙虾调料包', '秘制配方调料，在家也能做出餐厅味道。', 15.00, 300, 1, 4.6, 150),
(8, '荷叶糯米鸡', '荷叶包裹糯米鸡，清香软糯，特色美味。', 28.00, 120, 1, 4.7, 80);

-- ============================================
-- 为所有农产品插入图片（统一使用default.jpg）
-- ============================================
INSERT INTO agri_image (product_id, image_path, is_main, sort_order) VALUES
-- 昭阳公园 (产品ID: 1-3)
(1, '/nicon/default.jpg', 1, 1),
(2, '/nicon/default.jpg', 1, 1),
(3, '/nicon/default.jpg', 1, 1),
-- 大云山 (产品ID: 4-6)
(4, '/nicon/default.jpg', 1, 1),
(5, '/nicon/default.jpg', 1, 1),
(6, '/nicon/default.jpg', 1, 1),
-- 丽岭潭 (产品ID: 7-9)
(7, '/nicon/default.jpg', 1, 1),
(8, '/nicon/default.jpg', 1, 1),
(9, '/nicon/default.jpg', 1, 1),
-- 太阳岛 (产品ID: 10-12)
(10, '/nicon/default.jpg', 1, 1),
(11, '/nicon/default.jpg', 1, 1),
(12, '/nicon/default.jpg', 1, 1),
-- 佘湖山景区 (产品ID: 13-15)
(13, '/nicon/default.jpg', 1, 1),
(14, '/nicon/default.jpg', 1, 1),
(15, '/nicon/default.jpg', 1, 1),
-- 乌鸡岭景区 (产品ID: 16-18)
(16, '/nicon/default.jpg', 1, 1),
(17, '/nicon/default.jpg', 1, 1),
(18, '/nicon/default.jpg', 1, 1),
-- 大金旅游度假村 (产品ID: 19-21)
(19, '/nicon/default.jpg', 1, 1),
(20, '/nicon/default.jpg', 1, 1),
(21, '/nicon/default.jpg', 1, 1),
-- 小凯龙虾池 (产品ID: 22-24)
(22, '/nicon/default.jpg', 1, 1),
(23, '/nicon/default.jpg', 1, 1),
(24, '/nicon/default.jpg', 1, 1);

-- ============================================
-- 插入农产品评论数据
-- ============================================
INSERT INTO agri_comment (product_id, username, content, rating) VALUES
-- 邵东黄花菜评论
(1, 'zhangsan', '黄花菜很新鲜，做菜很香，家人都喜欢吃。', 5),
(1, 'lisi', '包装很好，没有碎，颜色金黄，品质不错。', 4),
-- 昭阳湖莲子评论
(2, 'wangwu', '莲子颗粒饱满，煮粥很香，清心安神效果好。', 5),
(2, 'zhangsan', '去芯率高，煮出来软糯香甜，值得购买。', 5),
-- 邵东茶油评论
(3, 'lisi', '茶油很香，炒菜油烟少，营养价值高。', 5),
(3, 'wangwu', '本地特产，纯正无添加，送礼自用都不错。', 4),
-- 云山云雾茶评论
(4, 'zhangsan', '茶叶清香持久，回甘悠长，是好茶。', 5),
(4, 'lisi', '泡出来茶汤清澈，香气扑鼻，物有所值。', 5),
-- 野生山茶菇评论
(5, 'wangwu', '菌菇肉质厚实，煲汤很鲜美，香味浓郁。', 4),
(5, 'zhangsan', '野生菌菇就是不一样，口感很好，营养丰富。', 5),
-- 山竹笋干评论
(6, 'lisi', '笋干很嫩，泡发后炒菜煲汤都很好吃。', 5),
(6, 'wangwu', '大云山的特产，品质有保障，回购多次了。', 4),
-- 丽岭野生鱼干评论
(7, 'zhangsan', '鱼干肉质紧实，蒸着吃很香，下饭佳品。', 5),
(7, 'lisi', '野生鱼的香味就是不一样，很有嚼劲。', 4),
-- 潭边水芹菜评论
(8, 'wangwu', '水芹菜很嫩，清香脆嫩，炒肉很好吃。', 5),
(8, 'zhangsan', '无污染的蔬菜，吃起来放心，口感很好。', 5),
-- 莲子藕粉评论
(9, 'lisi', '藕粉细腻爽滑，冲泡方便，早餐吃很不错。', 4),
(9, 'wangwu', '丽岭潭的藕粉品质好，营养丰富，老少皆宜。', 5),
-- 太阳岛柑橘评论
(10, 'zhangsan', '柑橘很甜，汁多味美，新鲜度很高。', 5),
(10, 'lisi', '太阳岛的柑橘名不虚传，甜度高，口感好。', 5),
(10, 'wangwu', '买了一箱，家人都说好吃，准备再回购。', 5),
-- 岛产红薯干评论
(11, 'zhangsan', '红薯干软糯香甜，天然健康，零食首选。', 4),
(11, 'lisi', '没有添加剂，吃起来放心，口感很好。', 5),
-- 太阳岛蜂蜜评论
(12, 'wangwu', '蜂蜜香甜浓郁，纯天然采集，品质很好。', 5),
(12, 'zhangsan', '百花蜜香味独特，每天早上喝一杯，对身体好。', 5),
-- 佘湖山毛尖评论
(13, 'lisi', '毛尖茶芽叶细嫩，清香持久，是好茶。', 5),
(13, 'wangwu', '佘湖山的茶叶品质不错，泡出来茶汤清澈。', 4),
-- 野生板栗评论
(14, 'zhangsan', '板栗香甜软糯，营养丰富，秋天必备。', 5),
(14, 'lisi', '野生板栗就是好吃，煮熟后很容易剥壳。', 5),
-- 山核桃评论
(15, 'wangwu', '山核桃壳薄仁满，香脆可口，补脑佳品。', 4),
(15, 'zhangsan', '佘湖山的特产，品质有保障，送礼不错。', 5),
-- 乌鸡岭土鸡评论
(16, 'lisi', '土鸡肉质紧实，煲汤很鲜美，汤鲜味美。', 5),
(16, 'wangwu', '散养的土鸡就是不一样，肉质有嚼劲。', 5),
-- 土鸡蛋评论
(17, 'zhangsan', '土鸡蛋蛋黄饱满，颜色金黄，营养丰富。', 5),
(17, 'lisi', '炒鸡蛋很香，孩子很喜欢吃，回购多次。', 5),
(17, 'wangwu', '乌鸡岭的土鸡蛋品质好，新鲜度高。', 5),
-- 野生葛根粉评论
(18, 'zhangsan', '葛根粉清热解毒，美容养颜，女士必备。', 4),
(18, 'lisi', '冲泡方便，口感细腻，有淡淡的葛根香味。', 5),
-- 大金有机大米评论
(19, 'wangwu', '有机大米米粒饱满，煮出来香软可口。', 5),
(19, 'zhangsan', '度假村的有机种植，吃起来放心，米香味浓。', 5),
-- 农家腊肉评论
(20, 'lisi', '腊肉烟熏香味浓郁，肥而不腻，炒菜很香。', 5),
(20, 'wangwu', '传统手工腌制，味道正宗，很下饭。', 5),
-- 度假村蔬菜干评论
(21, 'zhangsan', '蔬菜干绿色健康，煲汤炒菜皆宜，很方便。', 4),
(21, 'lisi', '有机蔬菜晒干，营养丰富，口感不错。', 5),
-- 小凯小龙虾评论
(22, 'wangwu', '小龙虾肉质鲜嫩，壳薄肉多，非常新鲜。', 5),
(22, 'zhangsan', '本地养殖的小龙虾，品质有保障，麻辣鲜香。', 5),
(22, 'lisi', '虾肉Q弹，味道很好，夏天必备美食。', 5),
-- 龙虾调料包评论
(23, 'zhangsan', '调料包秘制配方，在家也能做出餐厅味道。', 4),
(23, 'wangwu', '使用方便，味道正宗，做龙虾必备。', 5),
-- 荷叶糯米鸡评论
(24, 'lisi', '荷叶包裹糯米鸡，清香软糯，特色美味。', 5),
(24, 'zhangsan', '糯米鸡馅料丰富，荷叶香味浓郁，很好吃。', 4);
