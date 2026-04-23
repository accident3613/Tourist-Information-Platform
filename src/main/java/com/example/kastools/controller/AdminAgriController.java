package com.example.kastools.controller;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.example.kastools.entity.Admin;
import com.example.kastools.entity.AgriProduct;
import com.example.kastools.entity.Result;
import com.example.kastools.service.AdminPermissionService;
import com.example.kastools.service.AgriProductService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/admin/agri")
public class AdminAgriController {

    @Autowired
    private AgriProductService agriProductService;

    @Autowired
    private AdminPermissionService permissionService;

    @GetMapping("/list")
    public Result getAgriProducts(
            @RequestParam(value = "siteId", required = false) Long siteId,
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
            HttpServletRequest request) {
        // 获取当前管理员
        Admin admin = permissionService.getCurrentAdmin(request);
        if (admin == null) {
            Result result = new Result();
            result.setCode(0);
            result.setData("请先登录");
            return result;
        }

        Result result = new Result();
        try {
            List<AgriProduct> products;
            int total = 0;
            int totalPages = 0;

            // 运营人员只能查看自己景点的农特产
            if (permissionService.isOperator(admin)) {
                if (admin.getSite_id() != null) {
                    products = agriProductService.getProductsBySiteId(admin.getSite_id());
                    total = products.size();
                    totalPages = (int) Math.ceil((double) total / pageSize);
                } else {
                    result.setCode(0);
                    result.setData("未分配管理景点");
                    return result;
                }
            } else {
                // 超级管理员和管理员可以查看所有或指定景点的农特产
                if (siteId != null) {
                    products = agriProductService.getProductsBySiteId(siteId);
                    total = products.size();
                    totalPages = (int) Math.ceil((double) total / pageSize);
                } else {
                    Map<String, Object> pagingResult = agriProductService.getAllProductsWithPaging(page, pageSize);
                    products = (List<AgriProduct>) pagingResult.get("list");
                    total = (Integer) pagingResult.get("total");
                    totalPages = (Integer) pagingResult.get("totalPages");
                }
            }

            JSONObject responseData = new JSONObject();
            responseData.put("list", products);
            responseData.put("total", total);
            responseData.put("totalPages", totalPages);

            result.setCode(1);
            result.setData(JSON.toJSONString(responseData));
        } catch (Exception e) {
            result.setCode(0);
            result.setData("获取农特产失败");
        }
        return result;
    }

    @PostMapping("/add")
    public Result addAgriProduct(@RequestBody String body, HttpServletRequest request) {
        JSONObject json = JSON.parseObject(body);
        Long siteId = json.getLong("site_id");

        // 检查权限
        Result checkResult = permissionService.checkSitePermission(request, siteId);
        if (checkResult.getCode() != 1) {
            return checkResult;
        }

        AgriProduct product = new AgriProduct();
        product.setSite_id(siteId);
        product.setName(json.getString("name"));
        product.setDescription(json.getString("description"));
        product.setPrice_per_jin(json.getBigDecimal("price_per_jin"));
        product.setStock(json.getInteger("stock"));
        product.setStatus(json.getInteger("status") != null ? json.getInteger("status") : 1);
        product.setRating(BigDecimal.valueOf(5.0));
        product.setSales_count(0);

        return agriProductService.addProduct(product);
    }

    @PutMapping("/update")
    public Result updateAgriProduct(@RequestBody String body, HttpServletRequest request) {
        JSONObject json = JSON.parseObject(body);
        Long siteId = json.getLong("site_id");

        // 检查权限
        Result checkResult = permissionService.checkSitePermission(request, siteId);
        if (checkResult.getCode() != 1) {
            return checkResult;
        }

        // 先获取原有产品信息
        AgriProduct existingProduct = agriProductService.getProductById(json.getLong("id"));
        if (existingProduct == null) {
            Result result = new Result();
            result.setCode(0);
            result.setData("农特产不存在");
            return result;
        }

        AgriProduct product = new AgriProduct();
        product.setId(json.getLong("id"));
        product.setSite_id(siteId);
        product.setName(json.getString("name"));
        product.setDescription(json.getString("description"));
        product.setPrice_per_jin(json.getBigDecimal("price_per_jin"));
        product.setStock(json.getInteger("stock"));
        // 保留原有的status和rating
        product.setStatus(json.getInteger("status") != null ? json.getInteger("status") : existingProduct.getStatus());
        product.setRating(json.getBigDecimal("rating") != null ? json.getBigDecimal("rating") : existingProduct.getRating());
        product.setSales_count(existingProduct.getSales_count());

        return agriProductService.updateProduct(product);
    }

    @DeleteMapping("/delete/{id}")
    public Result deleteAgriProduct(@PathVariable Long id, HttpServletRequest request) {
        // 先获取农特产信息
        AgriProduct product = agriProductService.getProductById(id);
        if (product == null) {
            Result result = new Result();
            result.setCode(0);
            result.setData("农特产不存在");
            return result;
        }

        // 检查权限
        Result checkResult = permissionService.checkSitePermission(request, product.getSite_id());
        if (checkResult.getCode() != 1) {
            return checkResult;
        }

        return agriProductService.deleteProduct(id);
    }

    @PutMapping("/toggle/{id}")
    public Result toggleAgriStatus(@PathVariable Long id, HttpServletRequest request) {
        // 先获取农特产信息
        AgriProduct product = agriProductService.getProductById(id);
        if (product == null) {
            Result result = new Result();
            result.setCode(0);
            result.setData("农特产不存在");
            return result;
        }

        // 检查权限
        Result checkResult = permissionService.checkSitePermission(request, product.getSite_id());
        if (checkResult.getCode() != 1) {
            return checkResult;
        }

        return agriProductService.toggleProductStatus(id);
    }
}
