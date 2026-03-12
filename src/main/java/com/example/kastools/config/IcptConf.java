package com.example.kastools.config;
import com.example.kastools.interceptor.log_IC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class IcptConf implements WebMvcConfigurer {
    @Autowired
    log_IC log1;
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(log1)
                .addPathPatterns("/**")
                .excludePathPatterns("/user/login","/user/register","/");
    }

}
