package com.example.kastools.interceptor;

import com.example.kastools.mapper.UsrMap;
import com.example.kastools.properties.Jwtpro;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import java.nio.charset.StandardCharsets;

@Component
public class log_IC implements HandlerInterceptor {
    @Autowired
    UsrMap usrMap;
    @Autowired
    StringRedisTemplate stringRedisTemplate;
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)throws Exception {
        String jwt = request.getHeader("token");
        //判断当前拦截到的是Controller的方法还是其他资源
        if (!(handler instanceof HandlerMethod)) {
            //当前拦截到的不是动态方法，直接放行
            System.out.println("static method");
            return true;
        }

        // 如果是/error请求，直接放行（Spring Boot错误处理）
        if ("/error".equals(request.getRequestURI())) {
            System.out.println("错误处理请求，直接放行");
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
        // 检查token是否为空
        if (jwt == null || jwt.trim().isEmpty()) {
            System.out.println("拦截到未登录请求：token为空");
            System.out.println("请求方法: " + request.getMethod() + ", 请求URI: " + request.getRequestURI());
            response.setStatus(401);
            response.getWriter().write("false");
            return false;
        }
        try {
            Claims claims = Jwts.parser()
                    .setSigningKey(Jwtpro.secret_key.getBytes(StandardCharsets.UTF_8))
                    .build()
                    .parseClaimsJws(jwt)
                    .getBody();
            String username = (String) claims.get("username");
            String ip = request.getRemoteAddr();
            String redis_val=stringRedisTemplate.opsForValue().get("login:"+username);
            String user_ip=stringRedisTemplate.opsForValue().get(username+"_user_ip");

            if(!ip.equals(user_ip)) {
                if (!(ip.equals("0:0:0:0:0:0:0:1") || ip.equals("127.0.0.1")) && (user_ip.equals("0:0:0:0:0:0:0:1") || user_ip.equals("127.0.0.1")))
                {System.out.println("用户已在其他地方登录");
                System.out.println("登录ip："+user_ip+" "+ip);
                response.setStatus(401);
                response.getWriter().write("用户已在其他地方登录");
                return false;}
            }
            if(redis_val != null)
            {if(redis_val.equals("true"))
            {System.out.println("Redis记录已登录");
                return true;}
            if(redis_val.equals("false"))
                return false;
            }
            if(usrMap.chkmap(username)==1)
                return true;
            else
                return false;
        } catch (Exception e) {
            System.out.println("拦截到未登录请求"+e);
            System.out.println("请求方法: " + request.getMethod() + ", 请求URI: " + request.getRequestURI());
            response.setStatus(401);
            response.getWriter().write("false");
            return false;
        }
    }
}
