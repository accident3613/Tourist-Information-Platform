package com.example.kastools.controller;

import com.example.kastools.entity.Result;
import com.example.kastools.entity.User;
import com.example.kastools.mapper.UsrMap;
import com.example.kastools.properties.Jwtpro;
import com.example.kastools.utils.Jwt;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@RestController()
@RequestMapping("/user")
public class UserLogin {
    @Autowired
    UsrMap usrMap;
    @Autowired
    Jwt jwt;
    @PostMapping(value = "/login")
    public Result login(@RequestBody User user)
    {System.out.println(user.getUsername()+" "+user.getPassword());
int code=usrMap.logmap(user.getUsername(),user.getPassword());
String data;
Result result=new Result();
result.setCode(code);
if(code==1)
{
    data= jwt.create(user);
    result.setData(data);
}

return result;
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
    User user=new User();
try{
 user= usrMap.prfmap(username);

return user;
}
catch (Exception e)
{
    throw e;
}

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


}
