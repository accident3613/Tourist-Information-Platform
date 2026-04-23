package com.example.kastools.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Activity {
    private Long id;
    private String title;
    private String description;
    private String imageUrl;
    private Long siteId;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Long createdBy;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    
    private String siteName;
}
