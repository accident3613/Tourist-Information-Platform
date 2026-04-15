package com.example.kastools.mapper;

import com.example.kastools.entity.AgriImage;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface AgriImageMapper {

    @Select("SELECT * FROM agri_image WHERE product_id = #{productId} ORDER BY sort_order ASC")
    List<AgriImage> findByProductId(Long productId);

    @Select("SELECT * FROM agri_image WHERE product_id = #{productId} AND is_main = 1 LIMIT 1")
    AgriImage findMainImage(Long productId);

    @Insert("INSERT INTO agri_image (product_id, image_path, is_main, sort_order) " +
            "VALUES (#{product_id}, #{image_path}, #{is_main}, #{sort_order})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(AgriImage image);

    @Delete("DELETE FROM agri_image WHERE id = #{id}")
    int delete(Long id);

    @Delete("DELETE FROM agri_image WHERE product_id = #{productId}")
    int deleteByProductId(Long productId);
}
