package com.example.kastools.service;

import com.example.kastools.entity.AgriOrder;
import com.example.kastools.entity.Result;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;
import java.util.Map;

public interface AgriOrderService {
    Result createOrder(Long productId, Integer quantityJin, HttpServletRequest request);
    Result confirmPayment(String orderNo);
    Result cancelOrder(String orderNo);
    AgriOrder getOrderById(Long id);
    List<Map<String, Object>> getUserOrders(HttpServletRequest request);
    List<Map<String, Object>> getAllOrders();
}
