package com.example.kastools.service;

import com.example.kastools.entity.AgriProduct;
import com.example.kastools.entity.Result;

import java.util.List;

public interface AgriProductService {
    List<AgriProduct> getProductsBySiteId(Long siteId);
    AgriProduct getProductById(Long id);
    List<AgriProduct> getAllProducts(int start);
    Result addProduct(AgriProduct product);
    Result updateProduct(AgriProduct product);
    Result deleteProduct(Long id);
    Result toggleProductStatus(Long id);
}
