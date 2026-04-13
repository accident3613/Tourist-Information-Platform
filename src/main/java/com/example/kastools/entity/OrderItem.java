package com.example.kastools.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderItem {
    private Long id;
    private String order_id;
    private Long ticket_id;
    private String ticket_name;
    private Double ticket_price;
    private Integer quantity;
    private Double subtotal;
    private LocalDateTime create_time;
}
