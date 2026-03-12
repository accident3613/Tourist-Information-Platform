package com.example.kastools;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@EnableCaching
@SpringBootApplication
public class KastoolsApplication {

    public static void main(String[] args) {
        SpringApplication.run(KastoolsApplication.class, args);
    }

}
