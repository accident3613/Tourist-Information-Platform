package com.example.kastools.service.impl;

import com.alibaba.fastjson2.JSON;
import com.example.kastools.entity.AgriOrder;
import com.example.kastools.entity.AgriProduct;
import com.example.kastools.entity.Result;
import com.example.kastools.mapper.AgriOrderMapper;
import com.example.kastools.mapper.AgriProductMapper;
import com.example.kastools.service.AgriOrderService;
import com.example.kastools.utils.Jwt;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class AgriOrderServiceImpl implements AgriOrderService {

    @Autowired
    private AgriOrderMapper agriOrderMapper;

    @Autowired
    private AgriProductMapper agriProductMapper;

    @Autowired
    private Jwt jwt;

    @Override
    @Transactional
    public Result createOrder(Long productId, Integer quantityJin, HttpServletRequest request) {
        Result result = new Result();

        String token = request.getHeader("token");
        String username = jwt.getusn(token);

        AgriProduct product = agriProductMapper.findById(productId);
        if (product == null) {
            result.setCode(0);
            result.setData("产品不存在");
            return result;
        }

        if (product.getStock() != null && product.getStock() > 0 && product.getStock() < quantityJin) {
            result.setCode(0);
            result.setData("库存不足");
            return result;
        }

        String orderNo = "AGR" + System.currentTimeMillis();
        BigDecimal totalPrice = product.getPrice_per_jin().multiply(new BigDecimal(quantityJin));

        AgriOrder order = new AgriOrder();
        order.setOrder_no(orderNo);
        order.setUsername(username);
        order.setProduct_id(productId);
        order.setProduct_name(product.getName());
        order.setQuantity_jin(quantityJin);
        order.setPrice_per_jin(product.getPrice_per_jin());
        order.setTotal_price(totalPrice);
        order.setStatus("pending");

        int insertResult = agriOrderMapper.insert(order);

        if (insertResult <= 0) {
            result.setCode(0);
            result.setData("创建订单失败");
            return result;
        }

        Map<String, Object> data = new HashMap<>();
        data.put("orderNo", orderNo);
        data.put("totalPrice", totalPrice);
        data.put("productName", product.getName());
        data.put("quantityJin", quantityJin);

        result.setCode(1);
        result.setData(JSON.toJSONString(data));
        return result;
    }

    @Override
    @Transactional
    public Result confirmPayment(String orderNo) {
        Result result = new Result();

        AgriOrder order = agriOrderMapper.findByOrderNo(orderNo);
        if (order == null) {
            result.setCode(0);
            result.setData("订单不存在");
            return result;
        }

        // 减少库存
        int reduceResult = agriProductMapper.reduceStock(order.getProduct_id(), order.getQuantity_jin());
        if (reduceResult <= 0) {
            result.setCode(0);
            result.setData("库存不足");
            return result;
        }

        // 增加销量
        agriProductMapper.addSales(order.getProduct_id(), order.getQuantity_jin());

        // 更新订单状态
        int updateResult = agriOrderMapper.updateStatus(order.getId(), "completed");
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
    public Result cancelOrder(String orderNo) {
        Result result = new Result();

        AgriOrder order = agriOrderMapper.findByOrderNo(orderNo);
        if (order == null) {
            result.setCode(0);
            result.setData("订单不存在");
            return result;
        }

        if ("completed".equals(order.getStatus())) {
            result.setCode(0);
            result.setData("订单已完成，无法取消");
            return result;
        }

        if ("cancelled".equals(order.getStatus())) {
            result.setCode(0);
            result.setData("订单已取消");
            return result;
        }

        int updateResult = agriOrderMapper.updateStatus(order.getId(), "cancelled");
        if (updateResult > 0) {
            result.setCode(1);
            result.setData("订单已取消");
        } else {
            result.setCode(0);
            result.setData("取消失败");
        }
        return result;
    }

    @Override
    public AgriOrder getOrderById(Long id) {
        return agriOrderMapper.findById(id);
    }

    @Override
    public List<Map<String, Object>> getUserOrders(HttpServletRequest request) {
        String token = request.getHeader("token");
        String username = jwt.getusn(token);
        return agriOrderMapper.findByUsername(username);
    }

    @Override
    public List<Map<String, Object>> getAllOrders() {
        return agriOrderMapper.findAll();
    }
}
