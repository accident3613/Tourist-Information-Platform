package com.example.kastools.service.impl;

import com.alibaba.fastjson2.JSON;
import com.example.kastools.entity.Admin;
import com.example.kastools.entity.AdminLog;
import com.example.kastools.entity.Result;
import com.example.kastools.entity.User;
import com.example.kastools.mapper.AdminMapper;
import com.example.kastools.service.AdminService;
import com.example.kastools.utils.Jwt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
public class AdminServiceImpl implements AdminService {

    @Autowired
    private AdminMapper adminMapper;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private Jwt jwt;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Override
    public Result login(String username, String password) {
        Admin admin = adminMapper.findByUsername(username);
        if (admin == null) {
            Result result = new Result();
            result.setCode(0);
            result.setData("账号或密码错误");
            return result;
        }
        if (admin.getStatus() != 1) {
            Result result = new Result();
            result.setCode(0);
            result.setData("账号已被禁用");
            return result;
        }
        if (!passwordEncoder.matches(password, admin.getPassword())) {
            Result result = new Result();
            result.setCode(0);
            result.setData("账号或密码错误");
            return result;
        }

        // 更新最后登录时间
        adminMapper.updateLastLoginTime(admin.getId());

        // 生成JWT token
        User user = new User();
        user.setUsername(admin.getUsername());
        user.setIcon("");
        String token = jwt.create(user);

        // 存入Redis
        redisTemplate.opsForValue().set("admin:token:" + admin.getId(), token, 24, TimeUnit.HOURS);

        // 返回数据
        Map<String, Object> data = new HashMap<>();
        data.put("token", token);
        data.put("admin", admin);

        Result result = new Result();
        result.setCode(1);
        result.setData(JSON.toJSONString(data));
        return result;
    }

    @Override
    public Result logout(String token) {
        if (token != null) {
            try {
                String username = jwt.getusn(token);
                if (username != null) {
                    Admin admin = adminMapper.findByUsername(username);
                    if (admin != null) {
                        redisTemplate.delete("admin:token:" + admin.getId());
                    }
                }
            } catch (Exception e) {
                // 忽略解析错误
            }
        }
        Result result = new Result();
        result.setCode(1);
        result.setData("退出成功");
        return result;
    }

    @Override
    public Result getProfile(String token) {
        try {
            String username = jwt.getusn(token);
            Admin admin = adminMapper.findByUsername(username);
            if (admin != null) {
                admin.setPassword(null); // 不返回密码
                Result result = new Result();
                result.setCode(1);
                result.setData(JSON.toJSONString(admin));
                return result;
            }
        } catch (Exception e) {
            Result result = new Result();
            result.setCode(0);
            result.setData("获取信息失败");
            return result;
        }
        Result result = new Result();
        result.setCode(0);
        result.setData("管理员不存在");
        return result;
    }

    @Override
    public Result getAdminList() {
        List<Admin> list = adminMapper.findAll();
        list.forEach(admin -> admin.setPassword(null));
        Result result = new Result();
        result.setCode(1);
        result.setData(JSON.toJSONString(list));
        return result;
    }

    @Override
    public Result createAdmin(Admin admin) {
        // 检查用户名是否已存在
        Admin exist = adminMapper.findByUsername(admin.getUsername());
        if (exist != null) {
            Result result = new Result();
            result.setCode(0);
            result.setData("账号已存在");
            return result;
        }
        // 加密密码
        admin.setPassword(passwordEncoder.encode(admin.getPassword()));
        adminMapper.insert(admin);
        Result result = new Result();
        result.setCode(1);
        result.setData("创建成功");
        return result;
    }

    @Override
    public Result updateAdmin(Admin admin) {
        Admin exist = adminMapper.findById(admin.getId());
        if (exist == null) {
            Result result = new Result();
            result.setCode(0);
            result.setData("管理员不存在");
            return result;
        }
        // 不允许修改账号和密码
        admin.setUsername(null);
        admin.setPassword(null);
        adminMapper.update(admin);
        Result result = new Result();
        result.setCode(1);
        result.setData("更新成功");
        return result;
    }

    @Override
    public Result deleteAdmin(Long id) {
        Admin exist = adminMapper.findById(id);
        if (exist == null) {
            Result result = new Result();
            result.setCode(0);
            result.setData("管理员不存在");
            return result;
        }
        // 不允许删除超级管理员
        if ("super".equals(exist.getRole())) {
            Result result = new Result();
            result.setCode(0);
            result.setData("不能删除超级管理员");
            return result;
        }
        adminMapper.deleteById(id);
        Result result = new Result();
        result.setCode(1);
        result.setData("删除成功");
        return result;
    }

    @Override
    public Result resetPassword(Long id, String newPassword) {
        Admin exist = adminMapper.findById(id);
        if (exist == null) {
            Result result = new Result();
            result.setCode(0);
            result.setData("管理员不存在");
            return result;
        }
        String encodedPassword = passwordEncoder.encode(newPassword);
        adminMapper.updatePassword(id, encodedPassword);
        Result result = new Result();
        result.setCode(1);
        result.setData("密码重置成功");
        return result;
    }

    @Override
    public Result getLogs(int limit) {
        List<AdminLog> logs = adminMapper.findRecentLogs(limit);
        Result result = new Result();
        result.setCode(1);
        result.setData(JSON.toJSONString(logs));
        return result;
    }

    @Override
    public void addLog(Long adminId, String adminUsername, String action, String targetType, String targetId, String detail, String ip) {
        AdminLog log = new AdminLog();
        log.setAdminId(adminId);
        log.setAdminUsername(adminUsername);
        log.setAction(action);
        log.setTargetType(targetType);
        log.setTargetId(targetId);
        log.setDetail(detail);
        log.setIp(ip);
        adminMapper.insertLog(log);
    }
}
