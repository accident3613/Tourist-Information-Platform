package com.example.kastools.controller;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.example.kastools.entity.Result;
import com.example.kastools.service.AgriOrderService;
import com.example.kastools.utils.Jwt;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/agri/order")
public class AgriOrderController {

    @Autowired
    private AgriOrderService agriOrderService;

    @Autowired
    private Jwt jwt;

    @PostMapping("/create")
    public Result createOrder(
            @RequestParam("productId") Long productId,
            @RequestParam("quantityJin") Integer quantityJin,
            HttpServletRequest request) {
        return agriOrderService.createOrder(productId, quantityJin, request);
    }

    @PostMapping("/confirm")
    public Result confirmPayment(@RequestParam("orderNo") String orderNo) {
        return agriOrderService.confirmPayment(orderNo);
    }

    @PostMapping("/cancel")
    public Result cancelOrder(@RequestParam("orderNo") String orderNo) {
        return agriOrderService.cancelOrder(orderNo);
    }

    @GetMapping("/list")
    public Result getUserOrders(HttpServletRequest request) {
        Result result = new Result();
        try {
            List<Map<String, Object>> orders = agriOrderService.getUserOrders(request);
            result.setCode(1);
            result.setData(JSON.toJSONString(orders));
        } catch (Exception e) {
            result.setCode(0);
            result.setData("获取订单失败");
        }
        return result;
    }

    @GetMapping("/all")
    public Result getAllOrders(HttpServletRequest request) {
        String token = request.getHeader("token");
        String username = jwt.getusn(token);
        if (username == null) {
            Result result = new Result();
            result.setCode(0);
            result.setData("请先登录");
            return result;
        }

        Result result = new Result();
        try {
            List<Map<String, Object>> orders = agriOrderService.getAllOrders();
            result.setCode(1);
            result.setData(JSON.toJSONString(orders));
        } catch (Exception e) {
            result.setCode(0);
            result.setData("获取订单失败");
        }
        return result;
    }
}
