package com.example.kastools.mapper;

import com.example.kastools.entity.OrderItem;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface OrderMapper {

    @Insert("INSERT INTO orders (order_id, username, user_id, order_no, price, status, payment_status, payment_method, created_at) " +
            "VALUES (#{orderId}, #{username}, #{userId}, #{orderNo}, #{price}, #{status}, #{paymentStatus}, #{paymentMethod}, NOW())")
    int insertOrder(@Param("orderId") String orderId,
                    @Param("username") String username,
                    @Param("userId") String userId,
                    @Param("orderNo") String orderNo,
                    @Param("price") Double price,
                    @Param("status") String status,
                    @Param("paymentStatus") String paymentStatus,
                    @Param("paymentMethod") String paymentMethod);

    @Update("UPDATE orders SET status = 'confirmed', payment_status = 'paid', payment_time = NOW(), completed_at = NOW() WHERE order_id = #{orderId}")
    int confirmPayment(String orderId);

    @Select("SELECT * FROM orders WHERE order_id = #{orderId}")
    java.util.Map<String, Object> findOrderById(String orderId);

    @Select("SELECT * FROM order_item WHERE order_id = #{orderId}")
    List<OrderItem> findOrderItemsByOrderId(String orderId);

    @Select("SELECT * FROM orders WHERE username = #{username} ORDER BY created_at DESC")
    List<java.util.Map<String, Object>> findOrdersByUsername(String username);
}
