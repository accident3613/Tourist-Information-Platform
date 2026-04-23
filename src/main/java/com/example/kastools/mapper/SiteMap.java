package com.example.kastools.mapper;

import com.example.kastools.entity.SiteIcon;
import com.example.kastools.entity.Site_list;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Result;

import java.util.List;

@Mapper
public interface SiteMap {
    @Select("SELECT * FROM site_list LIMIT #{start},5")
    List<Site_list> list(int start);

    @Select("SELECT * FROM site_list WHERE id = #{site_id}")
    Site_list site(int site_id);

    @Insert("INSERT INTO collections (username,site_id) VALUES (#{username},#{site_id})")
    boolean col_add(String username,int site_id);

    @Delete("DELETE FROM collections WHERE username = #{username} and site_id = #{site_id}")
    boolean col_del(String username,int site_id);

    @Select("SELECT * FROM site WHERE site_id = #{site_id}")
    List<SiteIcon> siteicon(int site_id);

    @Select("SELECT * FROM site_list WHERE name LIKE CONCAT('%', #{keyword}, '%') LIMIT #{start},5")
    List<Site_list> searchByName(@Param("keyword") String keyword, @Param("start") int start);

    @Select("SELECT COUNT(*) FROM site_list WHERE name LIKE CONCAT('%', #{keyword}, '%')")
    int countByKeyword(@Param("keyword") String keyword);

    @Insert("INSERT INTO site_list (name, rating, icon, arating, number, description) VALUES (#{name}, #{rating}, #{icon}, #{arating}, #{number}, #{description})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insertSite(Site_list site);

    @Update("UPDATE site_list SET name = #{name}, rating = #{rating}, icon = COALESCE(#{icon}, icon), arating = #{arating}, description = #{description} WHERE id = #{id}")
    int updateSite(Site_list site);

    @Delete("DELETE FROM site_list WHERE id = #{id}")
    int deleteSite(int id);

    @Select("SELECT * FROM site_list WHERE rating >= #{minRating} AND rating < #{maxRating} LIMIT #{start},5")
    List<Site_list> findByRating(@Param("minRating") double minRating, @Param("maxRating") double maxRating, @Param("start") int start);

    @Select("SELECT s.*, COUNT(c.site_id) as collection_count FROM site_list s " +
            "LEFT JOIN collections c ON s.id = c.site_id " +
            "GROUP BY s.id " +
            "ORDER BY collection_count DESC " +
            "LIMIT #{limit}")
    @Results({
        @Result(property = "id", column = "id"),
        @Result(property = "name", column = "name"),
        @Result(property = "rating", column = "rating"),
        @Result(property = "icon", column = "icon"),
        @Result(property = "arating", column = "arating"),
        @Result(property = "number", column = "number"),
        @Result(property = "description", column = "description")
    })
    List<Site_list> findTopByCollectionCount(@Param("limit") int limit);

    @Select("SELECT * FROM site_list WHERE rating >= #{minRating} AND rating < #{maxRating} AND name LIKE CONCAT('%', #{keyword}, '%') LIMIT #{start},5")
    List<Site_list> searchByRatingAndKeyword(@Param("minRating") double minRating, @Param("maxRating") double maxRating, @Param("keyword") String keyword, @Param("start") int start);

    @Update("UPDATE site_list SET rating = #{rating} WHERE id = #{id}")
    int updateRating(@Param("id") int id, @Param("rating") double rating);

    @Update("UPDATE site_list SET number = number + 1 WHERE id = #{id}")
    int incrementNumber(@Param("id") int id);

    @Select("SELECT * FROM site_list ORDER BY id")
    List<Site_list> findAll();

    @Select("SELECT FLOOR(rating) as rating_range, COUNT(*) as count FROM site_list GROUP BY FLOOR(rating) ORDER BY rating_range")
    List<java.util.Map<String, Object>> getRatingDistribution();

    @Select("SELECT id, name, number as view_count FROM site_list ORDER BY number DESC LIMIT 10")
    List<java.util.Map<String, Object>> getTopSitesByViewCount();

    @Select("SELECT s.id, s.name, COALESCE(SUM(o.price), 0) as total_income FROM site_list s LEFT JOIN ticket t ON s.id = t.site_id LEFT JOIN order_item oi ON t.id = oi.ticket_id LEFT JOIN orders o ON CAST(oi.order_id AS CHAR) COLLATE utf8mb4_unicode_ci = CAST(o.order_id AS CHAR) COLLATE utf8mb4_unicode_ci GROUP BY s.id, s.name ORDER BY total_income DESC")
    List<java.util.Map<String, Object>> getSiteIncomeRanking();

    @Select("SELECT s.id, s.name, COUNT(DISTINCT o.order_id) as order_count FROM site_list s LEFT JOIN ticket t ON s.id = t.site_id LEFT JOIN order_item oi ON t.id = oi.ticket_id LEFT JOIN orders o ON CAST(oi.order_id AS CHAR) COLLATE utf8mb4_unicode_ci = CAST(o.order_id AS CHAR) COLLATE utf8mb4_unicode_ci GROUP BY s.id, s.name ORDER BY order_count DESC")
    List<java.util.Map<String, Object>> getSiteOrderCount();

    @Select("SELECT DATE(created_at) as date, COUNT(*) as order_count, COALESCE(SUM(price), 0) as total_amount FROM orders WHERE created_at >= DATE_SUB(CURDATE(), INTERVAL 6 DAY) GROUP BY DATE(created_at) ORDER BY date")
    List<java.util.Map<String, Object>> getOrderTrend7Days();
}
