package com.example.kastools.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@AllArgsConstructor         //生成有参构造
@NoArgsConstructor         //生成无参构造
@Data
public class User{
    public String username;
    public String password;
    public String icon;
    public Integer status;
    public String name;
}
