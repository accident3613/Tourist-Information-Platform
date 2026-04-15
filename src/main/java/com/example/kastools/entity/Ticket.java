package com.example.kastools.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Ticket {
    private Long id;
    private Long site_id;
    private String name;
    private Double price;
    private Double originalPrice;
    private Integer stock;
    private String description;
    private Integer status;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
