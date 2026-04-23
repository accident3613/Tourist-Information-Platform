package com.example.kastools.mapper;

import com.example.kastools.entity.Activity;
import org.apache.ibatis.annotations.*;

import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface ActivityMapper {

    @Insert("INSERT INTO activity (title, description, image_url, site_id, start_time, end_time, created_by) " +
            "VALUES (#{title}, #{description}, #{imageUrl}, #{siteId}, #{startTime}, #{endTime}, #{createdBy})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(Activity activity);

    @Update("UPDATE activity SET title = #{title}, description = #{description}, image_url = #{imageUrl}, " +
            "site_id = #{siteId}, start_time = #{startTime}, end_time = #{endTime} WHERE id = #{id}")
    int update(Activity activity);

    @Delete("DELETE FROM activity WHERE id = #{id}")
    int delete(Long id);

    @Select("SELECT a.*, s.name as site_name FROM activity a " +
            "LEFT JOIN site_list s ON a.site_id = s.id WHERE a.id = #{id}")
    @Results({
            @Result(property = "siteId", column = "site_id"),
            @Result(property = "imageUrl", column = "image_url"),
            @Result(property = "startTime", column = "start_time"),
            @Result(property = "endTime", column = "end_time"),
            @Result(property = "createdBy", column = "created_by"),
            @Result(property = "createTime", column = "create_time"),
            @Result(property = "updateTime", column = "update_time"),
            @Result(property = "siteName", column = "site_name")
    })
    Activity findById(Long id);

    @Select("SELECT a.*, s.name as site_name FROM activity a " +
            "LEFT JOIN site_list s ON a.site_id = s.id WHERE a.site_id = #{siteId}")
    @Results({
            @Result(property = "siteId", column = "site_id"),
            @Result(property = "imageUrl", column = "image_url"),
            @Result(property = "startTime", column = "start_time"),
            @Result(property = "endTime", column = "end_time"),
            @Result(property = "createdBy", column = "created_by"),
            @Result(property = "createTime", column = "create_time"),
            @Result(property = "updateTime", column = "update_time"),
            @Result(property = "siteName", column = "site_name")
    })
    Activity findBySiteId(Long siteId);

    @Select("SELECT a.*, s.name as site_name FROM activity a " +
            "LEFT JOIN site_list s ON a.site_id = s.id ORDER BY a.create_time DESC")
    @Results({
            @Result(property = "siteId", column = "site_id"),
            @Result(property = "imageUrl", column = "image_url"),
            @Result(property = "startTime", column = "start_time"),
            @Result(property = "endTime", column = "end_time"),
            @Result(property = "createdBy", column = "created_by"),
            @Result(property = "createTime", column = "create_time"),
            @Result(property = "updateTime", column = "update_time"),
            @Result(property = "siteName", column = "site_name")
    })
    List<Activity> findAll();

    @Select("SELECT a.*, s.name as site_name FROM activity a " +
            "LEFT JOIN site_list s ON a.site_id = s.id " +
            "WHERE a.start_time <= #{now} AND a.end_time >= #{now} " +
            "ORDER BY a.create_time DESC LIMIT #{limit}")
    @Results({
            @Result(property = "siteId", column = "site_id"),
            @Result(property = "imageUrl", column = "image_url"),
            @Result(property = "startTime", column = "start_time"),
            @Result(property = "endTime", column = "end_time"),
            @Result(property = "createdBy", column = "created_by"),
            @Result(property = "createTime", column = "create_time"),
            @Result(property = "updateTime", column = "update_time"),
            @Result(property = "siteName", column = "site_name")
    })
    List<Activity> findActiveActivities(@Param("now") LocalDateTime now, @Param("limit") int limit);

    @Select("SELECT a.*, s.name as site_name FROM activity a " +
            "LEFT JOIN site_list s ON a.site_id = s.id " +
            "WHERE a.site_id = #{siteId} AND a.start_time <= #{now} AND a.end_time >= #{now}")
    @Results({
            @Result(property = "siteId", column = "site_id"),
            @Result(property = "imageUrl", column = "image_url"),
            @Result(property = "startTime", column = "start_time"),
            @Result(property = "endTime", column = "end_time"),
            @Result(property = "createdBy", column = "created_by"),
            @Result(property = "createTime", column = "create_time"),
            @Result(property = "updateTime", column = "update_time"),
            @Result(property = "siteName", column = "site_name")
    })
    Activity findActiveBySiteId(@Param("siteId") Long siteId, @Param("now") LocalDateTime now);

    @Select("SELECT COUNT(*) FROM activity WHERE site_id = #{siteId} AND id != #{excludeId} " +
            "AND start_time <= #{now} AND end_time >= #{now}")
    int countActiveBySiteIdExclude(@Param("siteId") Long siteId, @Param("excludeId") Long excludeId, @Param("now") LocalDateTime now);

    @Select("SELECT COUNT(*) FROM activity WHERE site_id = #{siteId} AND start_time <= #{now} AND end_time >= #{now}")
    int countActiveBySiteId(@Param("siteId") Long siteId, @Param("now") LocalDateTime now);
}
