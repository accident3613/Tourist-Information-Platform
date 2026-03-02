package com.example.kastools.entity;

import lombok.Data;

@Data
public class Result {
private int code;  //1为成功 0为失败
private String data;  //返回数据
}
