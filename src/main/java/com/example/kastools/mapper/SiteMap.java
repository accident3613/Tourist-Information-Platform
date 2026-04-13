package com.example.kastools.mapper;

import com.example.kastools.entity.SiteIcon;
import com.example.kastools.entity.Site_list;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface SiteMap {
    @Select("SELECT * FROM site_list LIMIT #{start},5")    //获取5条
    List<Site_list> list(int start);

    @Select("SELECT * FROM site_list WHERE id = #{site_id}")  //获取指定
    Site_list site(int site_id);

    @Insert("INSERT INTO collections (username,site_id) VALUES (#{username},#{site_id})")
    boolean col_add(String username,int site_id);

    @Delete("DELETE FROM collections WHERE username = #{username} and site_id = #{site_id}")
    boolean col_del(String username,int site_id);

    @Select("SELECT * FROM site WHERE site_id = #{site_id}")    //获取景点照片
    List<SiteIcon> siteicon(int site_id);

    @Select("SELECT * FROM site_list WHERE name LIKE CONCAT('%', #{keyword}, '%') LIMIT #{start},5")
    List<Site_list> searchByName(@Param("keyword") String keyword, @Param("start") int start);

    @Select("SELECT COUNT(*) FROM site_list WHERE name LIKE CONCAT('%', #{keyword}, '%')")
    int countByKeyword(@Param("keyword") String keyword);

}
