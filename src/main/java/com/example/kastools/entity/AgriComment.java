package com.example.kastools.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AgriComment {
    private Long id;
    private Long product_id;
    private String username;
    private String content;
    private Integer rating;
    private LocalDateTime create_time;
}
