package com.example.kastools.mapper;

import com.example.kastools.entity.AgriProduct;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface AgriProductMapper {

    @Select("SELECT * FROM agri_product WHERE site_id = #{siteId} AND status = 1 ORDER BY sales_count DESC")
    List<AgriProduct> findBySiteId(Long siteId);

    @Select("SELECT * FROM agri_product WHERE id = #{id}")
    AgriProduct findById(Long id);

    @Select("SELECT * FROM agri_product ORDER BY id DESC")
    List<AgriProduct> findAll(@Param("start") int start);

    @Insert("INSERT INTO agri_product (site_id, name, description, price_per_jin, stock, status, rating, sales_count) " +
            "VALUES (#{site_id}, #{name}, #{description}, #{price_per_jin}, #{stock}, #{status}, #{rating}, #{sales_count})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(AgriProduct product);

    @Update("UPDATE agri_product SET name = #{name}, description = #{description}, price_per_jin = #{price_per_jin}, " +
            "stock = #{stock}, status = #{status}, rating = #{rating}, sales_count = #{sales_count} WHERE id = #{id}")
    int update(AgriProduct product);

    @Delete("DELETE FROM agri_product WHERE id = #{id}")
    int delete(Long id);

    @Update("UPDATE agri_product SET status = IF(status = 1, 0, 1) WHERE id = #{id}")
    int toggleStatus(Long id);

    @Update("UPDATE agri_product SET stock = stock - #{quantity} WHERE id = #{id} AND stock >= #{quantity}")
    int reduceStock(@Param("id") Long id, @Param("quantity") Integer quantity);

    @Update("UPDATE agri_product SET sales_count = sales_count + #{quantity} WHERE id = #{id}")
    int addSales(@Param("id") Long id, @Param("quantity") Integer quantity);

    @Update("UPDATE agri_product SET rating = #{rating} WHERE id = #{id}")
    int updateRating(@Param("id") Long id, @Param("rating") Double rating);
}
