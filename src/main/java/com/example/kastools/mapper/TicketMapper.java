package com.example.kastools.mapper;

import com.example.kastools.entity.OrderItem;
import com.example.kastools.entity.Ticket;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface TicketMapper {

    @Select("SELECT * FROM ticket WHERE site_id = #{siteId} AND status = 1")
    List<Ticket> findBySiteId(Long siteId);

    @Select("SELECT * FROM ticket WHERE id = #{id}")
    Ticket findById(Long id);

    @Update("UPDATE ticket SET stock = stock - #{quantity} WHERE id = #{ticketId} AND stock >= #{quantity}")
    int reduceStock(@Param("ticketId") Long ticketId, @Param("quantity") Integer quantity);

    @Insert("INSERT INTO order_item (order_id, ticket_id, ticket_name, ticket_price, quantity, subtotal, create_time) " +
            "VALUES (#{order_id}, #{ticket_id}, #{ticket_name}, #{ticket_price}, #{quantity}, #{subtotal}, NOW())")
    int insertOrderItem(OrderItem orderItem);
}
