package com.example.kastools.utils;

import com.example.kastools.entity.User;
import com.example.kastools.properties.Jwtpro;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class Jwt {

    public String create(User user){
        Map<String, Object> claims = new HashMap<>();
claims.put("username",user.getUsername());
claims.put("icon",user.getIcon());
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

        // 生成JWT的时间+持续时间
        long expMillis = System.currentTimeMillis() + Jwtpro.ttl;  //毫秒
        Date exp = new Date(expMillis);

        // 设置jwt的body
        JwtBuilder builder = Jwts.builder()
                // 如果有私有声明，一定要先设置这个自己创建的私有的声明，这个是给builder的claim赋值，一旦写在标准的声明赋值之后，就是覆盖了那些标准的声明的
                .setClaims(claims)
                // 设置签名使用的签名算法和签名使用的秘钥
                .signWith(signatureAlgorithm, Jwtpro.secret_key.getBytes(StandardCharsets.UTF_8))
                // 设置过期时间
                .setExpiration(exp);
        return builder.compact();
    }
    public String getusn(String token){
        Claims claims = Jwts.parser()
                .setSigningKey(Keys.hmacShaKeyFor(Jwtpro.secret_key.getBytes()))
                .build()
                .parseClaimsJws(token)
                .getBody();
        String username= (String) claims.get("username");
        return  username;
    }

}
