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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;
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
    @PostMapping(value = "/login")
    public Result login(@RequestBody User user,HttpServletRequest request)
    {
System.out.println(user.getUsername()+" "+user.getPassword());
int code=usrMap.logmap(user.getUsername(),user.getPassword());
String ip=request.getRemoteAddr();
String data;
Result result=new Result();
result.setCode(code);
if(code==1)
{   usrMap.lggmap(1,user.getUsername());
    data= jwt.create(user);
    result.setData(data);
    stringRedisTemplate.opsForValue().set("login:"+user.getUsername(),"true",5, TimeUnit.MINUTES);
    stringRedisTemplate.opsForValue().set(user.getUsername()+"_user_ip",ip,2,TimeUnit.HOURS);
}


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
public Result name(String name,HttpServletRequest request)
{
Result result=new Result();
String token=request.getHeader("token");
String username= jwt.getusn(token);
boolean status=usrMap.upnmap(name,username);
if(status)
{result.setCode(1);
result.setData("更新成功");
}
else
    result.setCode(0);
return result;
}
@PostMapping("/collect")   //添加收藏
public boolean collect(int site_id,HttpServletRequest request)
{
    String token=request.getHeader("token");
    String username= jwt.getusn(token);
return usrMap.colmap(site_id,username);

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
    {String token=request.getHeader("token");
        String username= jwt.getusn(token);
        return siteMap.col_add(username,site_id);
    }
    @GetMapping("/coldel")
    public boolean coldel(HttpServletRequest request,@RequestParam("site_id") int site_id)
    {String token=request.getHeader("token");
        String username= jwt.getusn(token);
        return siteMap.col_del(username,site_id);
    }



}
