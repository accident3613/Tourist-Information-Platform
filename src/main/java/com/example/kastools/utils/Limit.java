package com.example.kastools.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.concurrent.TimeUnit;

@Component
public class Limit {

    @Autowired
    StringRedisTemplate stringRedisTemplate;;
public boolean tryAcquire(String key,int limit,int exptime)
{
    Long count = stringRedisTemplate.opsForValue().increment(key);
    if(count == 1)
    {
        stringRedisTemplate.expire(key,exptime, TimeUnit.MINUTES);
    }
return count <= limit;
}



}
