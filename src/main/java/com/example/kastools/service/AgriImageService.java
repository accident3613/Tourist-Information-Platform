package com.example.kastools.service;

import com.example.kastools.entity.AgriImage;

import java.util.List;

public interface AgriImageService {
    List<AgriImage> getImagesByProductId(Long productId);
    AgriImage getMainImage(Long productId);
    boolean addImage(AgriImage image);
    boolean deleteImage(Long id);
    boolean deleteImagesByProductId(Long productId);
}
