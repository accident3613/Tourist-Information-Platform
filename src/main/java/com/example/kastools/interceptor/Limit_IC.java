package com.example.kastools.interceptor;

import com.example.kastools.utils.Jwt;
import com.example.kastools.utils.Limit;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
@Component
public class Limit_IC implements HandlerInterceptor {
    @Autowired
    Jwt jwt;
    @Autowired
    Limit limit;
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
    {
        String ip = request.getRemoteAddr();
        String uri = request.getRequestURI();
        String key = ip + ":" + uri;
        boolean status = limit.tryAcquire(key,100,1);
        if(!status)
        {
            System.out.println("IP: "+ip+" 访问  "+uri+" 速度过快");

        }
        return status;
    }

}
