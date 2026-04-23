package com.example.kastools.service;

import com.example.kastools.entity.AgriProduct;
import com.example.kastools.entity.Result;

import java.util.List;
import java.util.Map;

public interface AgriProductService {
    List<AgriProduct> getProductsBySiteId(Long siteId);
    AgriProduct getProductById(Long id);
    List<AgriProduct> getAllProducts(int start);
    Map<String, Object> getAllProductsWithPaging(int page, int pageSize);
    Result addProduct(AgriProduct product);
    Result updateProduct(AgriProduct product);
    Result deleteProduct(Long id);
    Result toggleProductStatus(Long id);
}
