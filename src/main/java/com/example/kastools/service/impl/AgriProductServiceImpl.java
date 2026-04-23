package com.example.kastools.service.impl;

import com.example.kastools.entity.AgriProduct;
import com.example.kastools.entity.Result;
import com.example.kastools.mapper.AgriProductMapper;
import com.example.kastools.service.AgriProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AgriProductServiceImpl implements AgriProductService {

    @Autowired
    private AgriProductMapper agriProductMapper;

    @Override
    public List<AgriProduct> getProductsBySiteId(Long siteId) {
        return agriProductMapper.findBySiteId(siteId);
    }

    @Override
    public AgriProduct getProductById(Long id) {
        return agriProductMapper.findById(id);
    }

    @Override
    public List<AgriProduct> getAllProducts(int start) {
        return agriProductMapper.findAll(start);
    }

    @Override
    public Map<String, Object> getAllProductsWithPaging(int page, int pageSize) {
        Map<String, Object> result = new HashMap<>();
        int offset = (page - 1) * pageSize;
        List<AgriProduct> list = agriProductMapper.findAllWithPaging(offset, pageSize);
        int total = agriProductMapper.countAll();
        int totalPages = (int) Math.ceil((double) total / pageSize);
        
        result.put("list", list);
        result.put("total", total);
        result.put("totalPages", totalPages);
        return result;
    }

    @Override
    public Result addProduct(AgriProduct product) {
        Result result = new Result();
        try {
            int rows = agriProductMapper.insert(product);
            if (rows > 0) {
                result.setCode(1);
                result.setData("添加成功");
            } else {
                result.setCode(0);
                result.setData("添加失败");
            }
        } catch (Exception e) {
            result.setCode(0);
            result.setData("添加失败: " + e.getMessage());
        }
        return result;
    }

    @Override
    public Result updateProduct(AgriProduct product) {
        Result result = new Result();
        try {
            int rows = agriProductMapper.update(product);
            if (rows > 0) {
                result.setCode(1);
                result.setData("更新成功");
            } else {
                result.setCode(0);
                result.setData("更新失败");
            }
        } catch (Exception e) {
            result.setCode(0);
            result.setData("更新失败: " + e.getMessage());
        }
        return result;
    }

    @Override
    public Result deleteProduct(Long id) {
        Result result = new Result();
        try {
            int rows = agriProductMapper.delete(id);
            if (rows > 0) {
                result.setCode(1);
                result.setData("删除成功");
            } else {
                result.setCode(0);
                result.setData("删除失败");
            }
        } catch (Exception e) {
            result.setCode(0);
            result.setData("删除失败: " + e.getMessage());
        }
        return result;
    }

    @Override
    public Result toggleProductStatus(Long id) {
        Result result = new Result();
        try {
            int rows = agriProductMapper.toggleStatus(id);
            if (rows > 0) {
                result.setCode(1);
                result.setData("状态更新成功");
            } else {
                result.setCode(0);
                result.setData("状态更新失败");
            }
        } catch (Exception e) {
            result.setCode(0);
            result.setData("状态更新失败: " + e.getMessage());
        }
        return result;
    }
}
