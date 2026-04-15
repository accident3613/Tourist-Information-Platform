package com.example.kastools.mapper;

import com.example.kastools.entity.Comment;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface CommentMapper {

    @Select("SELECT * FROM comment WHERE site_id = #{siteId} ORDER BY create_time DESC LIMIT #{start},10")
    List<Comment> findBySiteId(@Param("siteId") Long siteId, @Param("start") int start);

    @Select("SELECT COUNT(*) FROM comment WHERE site_id = #{siteId}")
    int countBySiteId(Long siteId);

    @Insert("INSERT INTO comment (site_id, username, content, rating, create_time) " +
            "VALUES (#{site_id}, #{username}, #{content}, #{rating}, NOW())")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(Comment comment);

    @Delete("DELETE FROM comment WHERE id = #{id}")
    int delete(Long id);

    @Select("SELECT AVG(rating) FROM comment WHERE site_id = #{siteId}")
    Double getAverageRating(Long siteId);

    @Select("SELECT * FROM comment WHERE id = #{id}")
    Comment findById(Long id);
}
