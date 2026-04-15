-- 插入默认管理员账户
-- 账号: admin
-- 密码: admin123 (明文存储)

INSERT INTO admin (username, password, name, role, status) 
SELECT 'admin', 'admin123', '超级管理员', 'super', 1
WHERE NOT EXISTS (SELECT 1 FROM admin WHERE username = 'admin');
