package com.example.kastools.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AgriImage {
    private Long id;
    private Long product_id;
    private String image_path;
    private Integer is_main;
    private Integer sort_order;
    private LocalDateTime create_time;
}
