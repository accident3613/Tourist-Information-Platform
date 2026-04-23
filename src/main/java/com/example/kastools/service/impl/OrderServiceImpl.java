package com.example.kastools.service.impl;

import com.alibaba.fastjson2.JSON;
import com.example.kastools.entity.Activity;
import com.example.kastools.entity.OrderItem;
import com.example.kastools.entity.Result;
import com.example.kastools.entity.Ticket;
import com.example.kastools.mapper.OrderMapper;
import com.example.kastools.mapper.SiteMap;
import com.example.kastools.mapper.TicketMapper;
import com.example.kastools.service.ActivityService;
import com.example.kastools.service.OrderService;
import com.example.kastools.utils.Jwt;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private TicketMapper ticketMapper;

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private SiteMap siteMap;

    @Autowired
    private Jwt jwt;

    @Autowired
    private ActivityService activityService;

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

        Double ticketPrice = ticket.getPrice();
        Activity activity = activityService.getActiveActivityBySiteId(ticket.getSite_id());
        if (activity != null && ticket.getPromoPrice() != null) {
            ticketPrice = ticket.getPromoPrice().doubleValue();
        }

        String orderId = UUID.randomUUID().toString();
        String orderNo = "ORD" + System.currentTimeMillis();
        Double totalPrice = ticketPrice * quantity;
        
        LocalDateTime expireDate = LocalDateTime.now().plusDays(15);

        int insertResult = orderMapper.insertOrder(
                orderId,
                username,
                username,
                orderNo,
                totalPrice,
                "pending",
                "unpaid",
                null,
                0,
                expireDate
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
        orderItem.setTicket_price(ticketPrice);
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
            // 支付成功，增加景区的number字段（去过的人数）
            for (OrderItem item : items) {
                Ticket ticket = ticketMapper.findById(item.getTicket_id());
                if (ticket != null && ticket.getSite_id() != null) {
                    siteMap.incrementNumber(ticket.getSite_id().intValue());
                }
            }
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

    @Override
    public Map<String, Object> getAllOrdersWithPaging(int page, int pageSize) {
        Map<String, Object> result = new HashMap<>();
        int offset = (page - 1) * pageSize;
        List<Map<String, Object>> orders = orderMapper.findAllOrdersWithPaging(offset, pageSize);
        int total = orderMapper.countAllOrders();
        int totalPages = (int) Math.ceil((double) total / pageSize);
        
        result.put("list", orders);
        result.put("total", total);
        result.put("page", page);
        result.put("pageSize", pageSize);
        result.put("totalPages", totalPages);
        return result;
    }

    @Override
    @Transactional
    public Result cancelOrder(String orderId) {
        Result result = new Result();

        Map<String, Object> order = orderMapper.findOrderById(orderId);
        if (order == null) {
            result.setCode(0);
            result.setData("订单不存在");
            return result;
        }

        String status = (String) order.get("status");
        if ("cancelled".equals(status)) {
            result.setCode(0);
            result.setData("订单已取消");
            return result;
        }

        if ("completed".equals(status)) {
            result.setCode(0);
            result.setData("订单已完成，无法取消");
            return result;
        }

        int updateResult = orderMapper.cancelOrder(orderId);
        if (updateResult > 0) {
            result.setCode(1);
            result.setData("订单已取消");
        } else {
            result.setCode(0);
            result.setData("取消失败");
        }
        return result;
    }
}
