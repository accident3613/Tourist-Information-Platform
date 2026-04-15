package com.example.kastools.service;

import com.example.kastools.entity.OrderItem;
import com.example.kastools.entity.Result;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;
import java.util.Map;

public interface OrderService {

    Result createOrder(Long ticketId, Integer quantity, HttpServletRequest request);

    Result confirmPayment(String orderId);

    Result cancelOrder(String orderId);

    Map<String, Object> getOrderDetail(String orderId);

    List<Map<String, Object>> getUserOrders(HttpServletRequest request);

    List<Map<String, Object>> getAllOrders();
}
