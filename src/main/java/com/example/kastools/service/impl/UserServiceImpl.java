package com.example.kastools.service.impl;

import com.example.kastools.entity.Result;
import com.example.kastools.entity.User;
import com.example.kastools.mapper.SiteMap;
import com.example.kastools.mapper.UsrMap;
import com.example.kastools.properties.Jwtpro;
import com.example.kastools.service.UserService;
import com.example.kastools.utils.Jwt;
import com.example.kastools.utils.UserProfile;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UsrMap usrMap;

    @Autowired
    private SiteMap siteMap;

    @Autowired
    private Jwt jwt;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private UserProfile userProfile;

    @Autowired
    private RedissonClient redissonClient;

    @Override
    public Result login(User user, HttpServletRequest request) {
        String ip = request.getRemoteAddr();
        String data;
        System.out.println(user.getUsername() + " " + user.getPassword());
        Result result = new Result();
        String status = stringRedisTemplate.opsForValue().get("logstatus:" + user.getUsername());
        //如果用户状态为false，则说明用户不存在
        if (status != null)
            if (status.equals("false")) {
                result.setCode(0);
                return result;
            }
        int code = usrMap.logmap(user.getUsername(), user.getPassword());
        result.setCode(code);

        if (code == 1) {
            usrMap.lggmap(1, user.getUsername());
            data = jwt.create(user);
            result.setData(data);
            stringRedisTemplate.opsForValue().set("login:" + user.getUsername(), "true", 5, TimeUnit.MINUTES);
            stringRedisTemplate.opsForValue().set(user.getUsername() + "_user_ip", ip, 2, TimeUnit.HOURS);
        }
        if (code == 0)
            stringRedisTemplate.opsForValue().set("logstatus:" + user.getUsername(), "false", 10, TimeUnit.SECONDS);
        return result;
    }

    @Override
    public Boolean logout(HttpServletRequest request) {
        String token = request.getHeader("token");
        String username = jwt.getusn(token);
        usrMap.lggmap(0, username);
        stringRedisTemplate.opsForValue().set("login:" + username, "false", 5, TimeUnit.MINUTES);
        stringRedisTemplate.delete(username + "_user_ip");

        return true;
    }

    @Override
    public Result register(User user) {
        Result result = new Result();
        try {
            usrMap.regmap(user.getUsername(), user.getPassword());
        } catch (Exception e) {
            result.setCode(0);
            result.setData("用户名已存在");
            return result;
        }
        result.setCode(1);
        result.setData("账户创建成功");
        return result;
    }

    @Override
    public User profile(HttpServletRequest request) {
        String token = request.getHeader("token");
        Claims claims =
                Jwts.parser()
                        .setSigningKey(Keys.hmacShaKeyFor(Jwtpro.secret_key.getBytes()))
                        .build()
                        .parseClaimsJws(token)
                        .getBody();
        String username = jwt.getusn(token);
        return userProfile.pf(username);
    }

    @Override
    public Result updateName(String name, HttpServletRequest request) throws InterruptedException {
        RLock lock = redissonClient.getLock("upname");
        boolean status = false;
        Result result = new Result();
        try {
            boolean islock = lock.tryLock(2, 5, TimeUnit.SECONDS);
            String token = request.getHeader("token");
            String username = jwt.getusn(token);
            status = usrMap.upnmap(name, username);
        } finally {
            try {
                lock.unlock();
            } catch (Exception e) {
                System.out.println("无事发生");
            }
        }
        if (status) {
            result.setCode(1);
            result.setData("更新成功");
        } else
            result.setCode(0);
        return result;
    }

    @Override
    public List<Integer> getCollectionList(HttpServletRequest request) {
        String token = request.getHeader("token");
        String username = jwt.getusn(token);
        return usrMap.clsmap(username);
    }

    @Override
    public boolean addCollection(HttpServletRequest request, int siteId) {
        RLock lock = redissonClient.getLock("coladd");
        boolean status = false;
        boolean islock = false;
        try {
            String username;
            String token = request.getHeader("token");
            username = jwt.getusn(token);
            islock = lock.tryLock(2, 10, TimeUnit.SECONDS);  //最多等2s，最多持有10s
            if (usrMap.chkcol(username, siteId) == 0)
                status = usrMap.colmap(siteId, username);
            System.out.println("nihao");
        } catch (Exception e) {
            System.out.println("重复点击收藏");
        } finally {
            try {
                lock.unlock();
                System.out.println(siteId);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
        System.out.println(siteId);
        return status;
    }

    @Override
    public boolean deleteCollection(HttpServletRequest request, int siteId) {
        String token = request.getHeader("token");
        String username = jwt.getusn(token);
        return siteMap.col_del(username, siteId);
    }
}
