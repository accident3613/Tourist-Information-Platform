package com.example.kastools.utils;

import com.example.kastools.entity.User;
import com.example.kastools.mapper.UsrMap;
import com.example.kastools.properties.Jwtpro;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

@Component
public class UserProfile {
    @Autowired
    UsrMap usrMap;
    @Cacheable(value = "user_profile",key = "#username")
    public User pf(String username)
    {
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
}
