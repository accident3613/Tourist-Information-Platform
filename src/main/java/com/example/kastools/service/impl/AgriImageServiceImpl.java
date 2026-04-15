package com.example.kastools.service.impl;

import com.example.kastools.entity.AgriImage;
import com.example.kastools.mapper.AgriImageMapper;
import com.example.kastools.service.AgriImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AgriImageServiceImpl implements AgriImageService {

    @Autowired
    private AgriImageMapper agriImageMapper;

    @Override
    public List<AgriImage> getImagesByProductId(Long productId) {
        return agriImageMapper.findByProductId(productId);
    }

    @Override
    public AgriImage getMainImage(Long productId) {
        return agriImageMapper.findMainImage(productId);
    }

    @Override
    public boolean addImage(AgriImage image) {
        return agriImageMapper.insert(image) > 0;
    }

    @Override
    public boolean deleteImage(Long id) {
        return agriImageMapper.delete(id) > 0;
    }

    @Override
    public boolean deleteImagesByProductId(Long productId) {
        return agriImageMapper.deleteByProductId(productId) > 0;
    }
}
