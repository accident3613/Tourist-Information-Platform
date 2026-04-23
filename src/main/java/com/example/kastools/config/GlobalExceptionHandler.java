package com.example.kastools.config;

import com.example.kastools.entity.Result;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public Result handleException(Exception e) {
        Result result = new Result();
        result.setCode(0);
        result.setData("系统错误：" + e.getMessage());
        return result;
    }
}
