package com.example.kastools.service;

import com.alibaba.fastjson2.JSON;
import com.example.kastools.entity.Result;
import com.example.kastools.mapper.SiteMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class StatsService {

    @Autowired
    private SiteMap siteMap;

    public Result getRatingDistribution() {
        Result result = new Result();
        List<Map<String, Object>> data = siteMap.getRatingDistribution();
        result.setCode(1);
        result.setData(JSON.toJSONString(data));
        return result;
    }

    public Result getTopSitesByViewCount() {
        Result result = new Result();
        List<Map<String, Object>> data = siteMap.getTopSitesByViewCount();
        result.setCode(1);
        result.setData(JSON.toJSONString(data));
        return result;
    }

    public Result getSiteIncomeRanking() {
        Result result = new Result();
        List<Map<String, Object>> data = siteMap.getSiteIncomeRanking();
        result.setCode(1);
        result.setData(JSON.toJSONString(data));
        return result;
    }

    public Result getSiteOrderCount() {
        Result result = new Result();
        List<Map<String, Object>> data = siteMap.getSiteOrderCount();
        result.setCode(1);
        result.setData(JSON.toJSONString(data));
        return result;
    }

    public Result getAllSiteStats() {
        Result result = new Result();
        Map<String, Object> data = new HashMap<>();
        data.put("ratingDistribution", siteMap.getRatingDistribution());
        data.put("topSitesByView", siteMap.getTopSitesByViewCount());
        data.put("siteIncome", siteMap.getSiteIncomeRanking());
        data.put("siteOrderCount", siteMap.getSiteOrderCount());
        result.setCode(1);
        result.setData(JSON.toJSONString(data));
        return result;
    }

    public Result getOrderTrend7Days() {
        Result result = new Result();
        List<Map<String, Object>> dbData = siteMap.getOrderTrend7Days();
        
        java.time.LocalDate today = java.time.LocalDate.now();
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd");
        java.util.Calendar cal = java.util.Calendar.getInstance();
        
        Map<String, Map<String, Object>> dataMap = new java.util.LinkedHashMap<>();
        for (int i = 6; i >= 0; i--) {
            cal.setTime(new java.util.Date());
            cal.add(java.util.Calendar.DAY_OF_MONTH, -i);
            String dateStr = sdf.format(cal.getTime());
            Map<String, Object> dayData = new HashMap<>();
            dayData.put("date", dateStr);
            dayData.put("orderCount", 0);
            dayData.put("totalAmount", 0.0);
            dataMap.put(dateStr, dayData);
        }
        
        for (Map<String, Object> row : dbData) {
            Object dateObj = row.get("date");
            String dateStr = null;
            if (dateObj instanceof java.sql.Date) {
                dateStr = dateObj.toString();
            } else if (dateObj instanceof String) {
                dateStr = (String) dateObj;
            } else {
                dateStr = String.valueOf(dateObj);
            }
            if (dataMap.containsKey(dateStr)) {
                Map<String, Object> dayData = dataMap.get(dateStr);
                dayData.put("orderCount", row.get("order_count"));
                Object amountObj = row.get("total_amount");
                if (amountObj instanceof Number) {
                    dayData.put("totalAmount", ((Number) amountObj).doubleValue());
                } else {
                    dayData.put("totalAmount", 0.0);
                }
            }
        }
        
        result.setCode(1);
        result.setData(JSON.toJSONString(new java.util.ArrayList<>(dataMap.values())));
        return result;
    }
}
