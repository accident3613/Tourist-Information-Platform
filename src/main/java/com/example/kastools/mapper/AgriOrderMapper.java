package com.example.kastools.mapper;

import com.example.kastools.entity.AgriOrder;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.Map;

@Mapper
public interface AgriOrderMapper {

    @Insert("INSERT INTO agri_order (order_no, username, product_id, product_name, quantity_jin, price_per_jin, total_price, status, create_time) " +
            "VALUES (#{order_no}, #{username}, #{product_id}, #{product_name}, #{quantity_jin}, #{price_per_jin}, #{total_price}, #{status}, NOW())")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(AgriOrder order);

    @Select("SELECT * FROM agri_order WHERE id = #{id}")
    AgriOrder findById(Long id);

    @Select("SELECT * FROM agri_order WHERE order_no = #{orderNo}")
    AgriOrder findByOrderNo(String orderNo);

    @Select("SELECT * FROM agri_order WHERE username = #{username} ORDER BY create_time DESC")
    List<Map<String, Object>> findByUsername(String username);

    @Update("UPDATE agri_order SET status = #{status}, pay_time = NOW() WHERE id = #{id}")
    int updateStatus(@Param("id") Long id, @Param("status") String status);

    @Select("SELECT * FROM agri_order ORDER BY create_time DESC")
    List<Map<String, Object>> findAll();
}
