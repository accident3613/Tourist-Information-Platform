package com.example.kastools.mapper;

import com.example.kastools.entity.AgriComment;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface AgriCommentMapper {

    @Select("SELECT * FROM agri_comment WHERE product_id = #{productId} ORDER BY create_time DESC LIMIT #{start},10")
    List<AgriComment> findByProductId(@Param("productId") Long productId, @Param("start") int start);

    @Select("SELECT COUNT(*) FROM agri_comment WHERE product_id = #{productId}")
    int countByProductId(Long productId);

    @Insert("INSERT INTO agri_comment (product_id, username, content, rating, create_time) " +
            "VALUES (#{product_id}, #{username}, #{content}, #{rating}, NOW())")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(AgriComment comment);

    @Delete("DELETE FROM agri_comment WHERE id = #{id}")
    int delete(Long id);

    @Select("SELECT AVG(rating) FROM agri_comment WHERE product_id = #{productId}")
    Double getAverageRating(Long productId);

    @Select("SELECT * FROM agri_comment WHERE id = #{id}")
    AgriComment findById(Long id);
}
