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

    @Select("SELECT * FROM ticket WHERE site_id = #{siteId}")
    List<Ticket> findBySiteIdAll(Long siteId);

    @Select("SELECT * FROM ticket")
    List<Ticket> findAll();

    @Select("SELECT * FROM ticket ORDER BY id DESC LIMIT #{offset}, #{pageSize}")
    List<Ticket> findAllWithPaging(@Param("offset") int offset, @Param("pageSize") int pageSize);

    @Select("SELECT COUNT(*) FROM ticket")
    int countAll();

    @Insert("INSERT INTO ticket (site_id, name, price, original_price, stock, description, status, create_time) " +
            "VALUES (#{site_id}, #{name}, #{price}, #{originalPrice}, #{stock}, #{description}, #{status}, NOW())")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(Ticket ticket);

    @Update("UPDATE ticket SET site_id = #{site_id}, name = #{name}, price = #{price}, " +
            "original_price = #{originalPrice}, stock = #{stock}, description = #{description}, " +
            "status = #{status}, update_time = NOW() WHERE id = #{id}")
    int update(Ticket ticket);

    @Update("UPDATE ticket SET status = IF(status = 1, 0, 1) WHERE id = #{id}")
    int toggleStatus(Long id);

    @Update("UPDATE ticket SET promo_price = #{promoPrice} WHERE id = #{id}")
    int updatePromoPrice(@Param("id") Long id, @Param("promoPrice") java.math.BigDecimal promoPrice);

    @Update("UPDATE ticket SET promo_price = NULL WHERE id = #{id}")
    int clearPromoPrice(Long id);

    @Update("UPDATE ticket SET stock = stock - #{quantity} WHERE id = #{ticketId} AND stock >= #{quantity}")
    int reduceStock(@Param("ticketId") Long ticketId, @Param("quantity") Integer quantity);

    @Insert("INSERT INTO order_item (order_id, ticket_id, ticket_name, ticket_price, quantity, subtotal, create_time) " +
            "VALUES (#{order_id}, #{ticket_id}, #{ticket_name}, #{ticket_price}, #{quantity}, #{subtotal}, NOW())")
    int insertOrderItem(OrderItem orderItem);
}
