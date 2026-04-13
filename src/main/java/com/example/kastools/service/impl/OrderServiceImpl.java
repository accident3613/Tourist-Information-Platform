package com.example.kastools.service.impl;

import com.alibaba.fastjson2.JSON;
import com.example.kastools.entity.OrderItem;
import com.example.kastools.entity.Result;
import com.example.kastools.entity.Ticket;
import com.example.kastools.mapper.OrderMapper;
import com.example.kastools.mapper.TicketMapper;
import com.example.kastools.service.OrderService;
import com.example.kastools.utils.Jwt;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private TicketMapper ticketMapper;

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private Jwt jwt;

    @Override
    @Transactional
    public Result createOrder(Long ticketId, Integer quantity, HttpServletRequest request) {
        Result result = new Result();

        String token = request.getHeader("token");
        String username = jwt.getusn(token);

        Ticket ticket = ticketMapper.findById(ticketId);
        if (ticket == null) {
            result.setCode(0);
            result.setData("门票不存在");
            return result;
        }

        if (ticket.getStock() != null && ticket.getStock() > 0 && ticket.getStock() < quantity) {
            result.setCode(0);
            result.setData("库存不足");
            return result;
        }

        String orderId = UUID.randomUUID().toString();
        String orderNo = "ORD" + System.currentTimeMillis();
        Double totalPrice = ticket.getPrice() * quantity;

        int insertResult = orderMapper.insertOrder(
                orderId,
                username,
                username,
                orderNo,
                totalPrice,
                "pending",
                "unpaid",
                null
        );

        if (insertResult <= 0) {
            result.setCode(0);
            result.setData("创建订单失败");
            return result;
        }

        OrderItem orderItem = new OrderItem();
        orderItem.setOrder_id(orderId);
        orderItem.setTicket_id(ticketId);
        orderItem.setTicket_name(ticket.getName());
        orderItem.setTicket_price(ticket.getPrice());
        orderItem.setQuantity(quantity);
        orderItem.setSubtotal(totalPrice);

        ticketMapper.insertOrderItem(orderItem);

        Map<String, Object> data = new HashMap<>();
        data.put("orderId", orderId);
        data.put("orderNo", orderNo);
        data.put("totalPrice", totalPrice);
        data.put("ticketName", ticket.getName());
        data.put("quantity", quantity);

        result.setCode(1);
        result.setData(JSON.toJSONString(data));
        return result;
    }

    @Override
    @Transactional
    public Result confirmPayment(String orderId) {
        Result result = new Result();

        Map<String, Object> order = orderMapper.findOrderById(orderId);
        if (order == null) {
            result.setCode(0);
            result.setData("订单不存在");
            return result;
        }
        System.out.println(orderId);
        List<OrderItem> items = orderMapper.findOrderItemsByOrderId(orderId);
        System.out.println(items.get(0).toString());
        for (OrderItem item : items) {

            int reduceResult = ticketMapper.reduceStock(item.getTicket_id(), item.getQuantity());
            if (reduceResult <= 0) {
                System.out.println(item.getTicket_id()+item.getQuantity());
                result.setCode(0);
                result.setData("库存不足");
                return result;
            }
        }

        int updateResult = orderMapper.confirmPayment(orderId);
        if (updateResult > 0) {
            result.setCode(1);
            result.setData("支付成功");
        } else {
            result.setCode(0);
            result.setData("支付确认失败");
        }
        return result;
    }

    @Override
    public Map<String, Object> getOrderDetail(String orderId) {
        Map<String, Object> order = orderMapper.findOrderById(orderId);
        if (order != null) {
            List<OrderItem> items = orderMapper.findOrderItemsByOrderId(orderId);
            order.put("items", items);
        }
        return order;
    }

    @Override
    public List<Map<String, Object>> getUserOrders(HttpServletRequest request) {
        String token = request.getHeader("token");
        String username = jwt.getusn(token);
        return orderMapper.findOrdersByUsername(username);
    }
}
