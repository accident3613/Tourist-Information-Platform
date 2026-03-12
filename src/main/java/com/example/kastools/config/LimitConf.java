package com.example.kastools.config;

import com.example.kastools.interceptor.Limit_IC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class LimitConf implements WebMvcConfigurer {
@Autowired
    Limit_IC limitIc;
public void addInterceptors(InterceptorRegistry registry)
{
    registry.addInterceptor(limitIc)
            .addPathPatterns("/**");


}


}
