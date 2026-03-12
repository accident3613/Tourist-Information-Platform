package com.example.kastools.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@AllArgsConstructor         //生成有参构造
@NoArgsConstructor         //生成无参构造
@Data
public class Site_list{
    private int id;
    private String name;
    private float rating;
    private String icon;
    private float arating;
    private int number;
    private String description;
}
