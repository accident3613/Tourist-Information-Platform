package com.example.kastools.controller;

import com.example.kastools.entity.Collection;
import com.example.kastools.entity.Result;
import com.example.kastools.entity.User;
import com.example.kastools.mapper.SiteMap;
import com.example.kastools.mapper.UsrMap;
import com.example.kastools.properties.Jwtpro;
import com.example.kastools.utils.Jwt;
import com.example.kastools.utils.UserProfile;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@RestController()
@RequestMapping("/user")
public class UserC {
    @Autowired
    UsrMap usrMap;
    @Autowired
    SiteMap siteMap;
    @Autowired
    Jwt jwt;
    @Autowired
    StringRedisTemplate stringRedisTemplate;
    @Autowired
    UserProfile userProfile;
    @Autowired
    RedissonClient redissonClient;
    @PostMapping(value = "/login")
    public Result login(@RequestBody User user,HttpServletRequest request)
    {
String ip=request.getRemoteAddr();
String data;
System.out.println(user.getUsername()+" "+user.getPassword());
Result result=new Result();
String status=stringRedisTemplate.opsForValue().get("logstatus:"+user.getUsername());
//如果用户状态为false，则说明用户不存在
        if(status!=null)
if(status.equals("false"))
{
result.setCode(0);
return result;
        }
int code=usrMap.logmap(user.getUsername(),user.getPassword());
result.setCode(code);

if(code==1)
{   usrMap.lggmap(1,user.getUsername());
    data= jwt.create(user);
    result.setData(data);
    stringRedisTemplate.opsForValue().set("login:"+user.getUsername(),"true",5, TimeUnit.MINUTES);
    stringRedisTemplate.opsForValue().set(user.getUsername()+"_user_ip",ip,2,TimeUnit.HOURS);
}
if(code==0)
    stringRedisTemplate.opsForValue().set("logstatus:"+user.getUsername(),"false",10, TimeUnit.SECONDS);
return result;
    }

    @PostMapping(value = "/logout")
    public Boolean logout(HttpServletRequest request)
    {   String token=request.getHeader("token");
        String username=jwt.getusn(token);
  usrMap.lggmap(0, username);
            stringRedisTemplate.opsForValue().set("login:"+username,"false",5, TimeUnit.MINUTES);
            stringRedisTemplate.delete(username+"_user_ip");

        return true;
    }

    @PostMapping("/register")
            public Result register(@RequestBody User user)
    {
Result result=new Result();
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

@GetMapping("/profile")    //获取用户信息
    public User profile(HttpServletRequest request)
{
String token =request.getHeader("token");
Claims claims=
        Jwts.parser()
        .setSigningKey(Keys.hmacShaKeyFor(Jwtpro.secret_key.getBytes()))
        .build()
        .parseClaimsJws(token)
        .getBody();
String username= jwt.getusn(token);
    return userProfile.pf(username);

}
@PutMapping("/update-name")
public Result name(String name,HttpServletRequest request) throws InterruptedException {
        RLock lock=redissonClient.getLock("upname");
    boolean status=false;
    Result result=new Result();
    try{boolean islock=lock.tryLock(2,5,TimeUnit.SECONDS);
String token=request.getHeader("token");
String username= jwt.getusn(token);
status=usrMap.upnmap(name,username);}
    finally {
        try {
            lock.unlock();
        } catch (Exception e) {
            System.out.println("无事发生");
        }
    }
    if(status)
{result.setCode(1);
result.setData("更新成功");
}
else
    result.setCode(0);
return result;
}

    @PostMapping("/colist")   //获取收藏列表
    public List<Integer> colist(HttpServletRequest request)
    {
        String token=request.getHeader("token");
        String username= jwt.getusn(token);
        return usrMap.clsmap(username);
    }

    @GetMapping("/coladd")
    public boolean coladd(HttpServletRequest request,@RequestParam("site_id") int site_id)
    {RLock lock= redissonClient.getLock("coladd");
        boolean status=false;
        boolean islock=false;
        try{
            String username;
            String token=request.getHeader("token");
            username= jwt.getusn(token);
            islock=lock.tryLock(2, 10, TimeUnit.SECONDS);  //最多等2s，最多持有10s
            if(usrMap.chkcol(username,site_id)!=0)
            status=usrMap.colmap(site_id,username);

        }
        catch(Exception e)
        {System.out.println("重复点击收藏");
        }
        finally{
            try{
                lock.unlock();
            }
            catch (Exception e)
            {System.out.println(e.getMessage());}
        }

        return status;
    }
    @GetMapping("/coldel")
    public boolean coldel(HttpServletRequest request,@RequestParam("site_id") int site_id)
    {String token=request.getHeader("token");
        String username= jwt.getusn(token);
        return siteMap.col_del(username,site_id);
    }



}
