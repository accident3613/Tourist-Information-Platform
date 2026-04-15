package com.example.kastools.controller;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.example.kastools.entity.AgriProduct;
import com.example.kastools.entity.Result;
import com.example.kastools.service.AgriImageService;
import com.example.kastools.service.AgriProductService;
import com.example.kastools.utils.Jwt;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/agri")
public class AgriProductController {

    @Autowired
    private AgriProductService agriProductService;

    @Autowired
    private AgriImageService agriImageService;

    @Autowired
    private Jwt jwt;

    @GetMapping("/list")
    public Result getProductsBySiteId(@RequestParam("siteId") Long siteId) {
        Result result = new Result();
        try {
            List<AgriProduct> products = agriProductService.getProductsBySiteId(siteId);
            result.setCode(1);
            result.setData(JSON.toJSONString(products));
        } catch (Exception e) {
            result.setCode(0);
            result.setData("获取农产品失败");
        }
        return result;
    }

    @GetMapping("/detail")
    public Result getProductDetail(@RequestParam("id") Long id) {
        Result result = new Result();
        try {
            AgriProduct product = agriProductService.getProductById(id);
            if (product != null) {
                result.setCode(1);
                result.setData(JSON.toJSONString(product));
            } else {
                result.setCode(0);
                result.setData("产品不存在");
            }
        } catch (Exception e) {
            result.setCode(0);
            result.setData("获取产品详情失败");
        }
        return result;
    }

    @GetMapping("/images")
    public Result getProductImages(@RequestParam("productId") Long productId) {
        Result result = new Result();
        try {
            result.setCode(1);
            result.setData(JSON.toJSONString(agriImageService.getImagesByProductId(productId)));
        } catch (Exception e) {
            result.setCode(0);
            result.setData("获取图片失败");
        }
        return result;
    }

    @PostMapping("/add")
    public Result addProduct(@RequestBody String body, HttpServletRequest request) {
        String token = request.getHeader("token");
        String username = jwt.getusn(token);
        if (username == null) {
            Result result = new Result();
            result.setCode(0);
            result.setData("请先登录");
            return result;
        }

        JSONObject json = JSON.parseObject(body);
        AgriProduct product = new AgriProduct();
        product.setSite_id(json.getLong("site_id"));
        product.setName(json.getString("name"));
        product.setDescription(json.getString("description"));
        product.setPrice_per_jin(json.getBigDecimal("price_per_jin"));
        product.setStock(json.getInteger("stock"));
        product.setStatus(1);
        product.setRating(java.math.BigDecimal.valueOf(5.0));
        product.setSales_count(0);

        return agriProductService.addProduct(product);
    }

    @PutMapping("/update")
    public Result updateProduct(@RequestBody String body, HttpServletRequest request) {
        String token = request.getHeader("token");
        String username = jwt.getusn(token);
        if (username == null) {
            Result result = new Result();
            result.setCode(0);
            result.setData("请先登录");
            return result;
        }

        JSONObject json = JSON.parseObject(body);
        AgriProduct product = new AgriProduct();
        product.setId(json.getLong("id"));
        product.setSite_id(json.getLong("site_id"));
        product.setName(json.getString("name"));
        product.setDescription(json.getString("description"));
        product.setPrice_per_jin(json.getBigDecimal("price_per_jin"));
        product.setStock(json.getInteger("stock"));
        product.setStatus(json.getInteger("status"));

        return agriProductService.updateProduct(product);
    }

    @DeleteMapping("/delete/{id}")
    public Result deleteProduct(@PathVariable Long id, HttpServletRequest request) {
        String token = request.getHeader("token");
        String username = jwt.getusn(token);
        if (username == null) {
            Result result = new Result();
            result.setCode(0);
            result.setData("请先登录");
            return result;
        }

        return agriProductService.deleteProduct(id);
    }
}
