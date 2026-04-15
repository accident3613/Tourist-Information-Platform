package com.example.kastools.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AgriOrder {
    private Long id;
    private String order_no;
    private String username;
    private Long product_id;
    private String product_name;
    private Integer quantity_jin;
    private BigDecimal price_per_jin;
    private BigDecimal total_price;
    private String status;
    private LocalDateTime create_time;
    private LocalDateTime pay_time;
}
