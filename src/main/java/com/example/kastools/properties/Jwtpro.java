package com.example.kastools.properties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


public class Jwtpro {
   public static String secret_key="itcast_secret_key_for_jwt_token_generation_2026"; // 32+ 字符的密钥
   public static long ttl=7200000;   //毫秒
}
