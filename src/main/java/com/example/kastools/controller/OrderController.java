package com.example.kastools.controller;

import com.alibaba.fastjson2.JSON;
import com.example.kastools.entity.Result;
import com.example.kastools.service.OrderService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping("/create")
    public Result create(@RequestParam("ticketId") Long ticketId,
                        @RequestParam("quantity") Integer quantity,
                        HttpServletRequest request) {
        return orderService.createOrder(ticketId, quantity, request);
    }

    @PostMapping("/confirm")
    public Result confirm(@RequestParam("orderId") String orderId) {
        return orderService.confirmPayment(orderId);
    }

    @GetMapping("/detail")
    public Result detail(@RequestParam("orderId") String orderId) {
        Result result = new Result();
        Map<String, Object> order = orderService.getOrderDetail(orderId);
        if (order != null) {
            result.setCode(1);
            result.setData(JSON.toJSONString(order));
        } else {
            result.setCode(0);
            result.setData("订单不存在");
        }
        return result;
    }

    @GetMapping("/list")
    public Result list(HttpServletRequest request) {
        Result result = new Result();
        List<Map<String, Object>> orders = orderService.getUserOrders(request);
        result.setCode(1);
        result.setData(JSON.toJSONString(orders));
        return result;
    }

    @PostMapping("/cancel")
    public Result cancel(@RequestParam("orderId") String orderId) {
        return orderService.cancelOrder(orderId);
    }
}
