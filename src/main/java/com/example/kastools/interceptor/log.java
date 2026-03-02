package com.example.kastools.interceptor;

import com.example.kastools.properties.Jwtpro;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class log implements HandlerInterceptor {
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)throws Exception {
        String jwt = request.getHeader("token");
        //判断当前拦截到的是Controller的方法还是其他资源
        if (!(handler instanceof HandlerMethod)) {
            //当前拦截到的不是动态方法，直接放行
            System.out.println("static method");
            return true;
        }
/*
// 强制转换为
 HandlerMethod HandlerMethod handlerMethod = (HandlerMethod) handler;
 // 获取 Controller 类名
 String controllerName = handlerMethod.getBeanType().getName();
 //获取方法名
 String methodName = handlerMethod.getMethod().getName();
 System.out.println("调用的 Controller: " + controllerName);
 System.out.println("调用的方法: " + methodName);

 */
        try {
            Claims claims = Jwts.parser()
                    .setSigningKey(Keys.hmacShaKeyFor(Jwtpro.secret_key.getBytes()))
                    .build()
                    .parseClaimsJws(jwt)
                    .getBody();
            System.out.println(claims);
            return true;
        } catch (Exception e) {
            System.out.println("拦截到未登录请求");
            response.setStatus(401);
            response.getWriter().write("false");
            return false;
        }
    }
}
