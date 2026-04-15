package com.example.kastools.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AgriProduct {
    private Long id;
    private Long site_id;
    private String name;
    private String description;
    private BigDecimal price_per_jin;
    private Integer stock;
    private Integer status;
    private BigDecimal rating;
    private Integer sales_count;
    private LocalDateTime create_time;
    private LocalDateTime update_time;
}
