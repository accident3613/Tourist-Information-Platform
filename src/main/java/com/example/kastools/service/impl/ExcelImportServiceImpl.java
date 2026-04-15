package com.example.kastools.service.impl;

import com.alibaba.fastjson2.JSON;
import com.example.kastools.entity.Admin;
import com.example.kastools.entity.Result;
import com.example.kastools.entity.Site_list;
import com.example.kastools.mapper.AdminMapper;
import com.example.kastools.mapper.AgriOrderMapper;
import com.example.kastools.mapper.OrderMapper;
import com.example.kastools.mapper.SiteMap;
import com.example.kastools.service.ExcelImportService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.OutputStreamWriter;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.*;

@Service
public class ExcelImportServiceImpl implements ExcelImportService {

    @Autowired
    private SiteMap siteMap;

    @Autowired
    private AdminMapper adminMapper;

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private AgriOrderMapper agriOrderMapper;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Override
    public Result importSites(MultipartFile file) {
        Result result = new Result();
        try {
            List<Site_list> sites = new ArrayList<>();
            List<String> errors = new ArrayList<>();
            
            BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8));
            String line;
            int lineNumber = 0;
            
            while ((line = reader.readLine()) != null) {
                lineNumber++;
                if (lineNumber == 1) continue; // 跳过表头
                
                if (line.trim().isEmpty()) continue;
                
                String[] fields = parseCSVLine(line);
                
                try {
                    Site_list site = new Site_list();
                    site.setName(fields.length > 0 ? fields[0].trim() : null);
                    site.setDescription(fields.length > 1 ? fields[1].trim() : null);
                    site.setRating(fields.length > 2 && !fields[2].trim().isEmpty() ? parseFloat(fields[2].trim(), 4.0f) : 4.0f);
                    site.setArating(fields.length > 3 && !fields[3].trim().isEmpty() ? parseFloat(fields[3].trim(), 5.0f) : 5.0f);
                    site.setNumber(fields.length > 4 && !fields[4].trim().isEmpty() ? parseInt(fields[4].trim(), 0) : 0);
                    site.setIcon(fields.length > 5 && !fields[5].trim().isEmpty() ? fields[5].trim() : "default.jpg");
                    
                    if (site.getName() == null || site.getName().isEmpty()) {
                        errors.add("第" + lineNumber + "行: 景点名称不能为空");
                        continue;
                    }
                    
                    sites.add(site);
                } catch (Exception e) {
                    errors.add("第" + lineNumber + "行: " + e.getMessage());
                }
            }
            
            reader.close();
            
            int successCount = 0;
            for (Site_list site : sites) {
                try {
                    siteMap.insertSite(site);
                    successCount++;
                } catch (Exception e) {
                    errors.add("景点 '" + site.getName() + "' 导入失败: " + e.getMessage());
                }
            }
            
            Map<String, Object> data = new HashMap<>();
            data.put("success", successCount);
            data.put("failed", errors.size());
            data.put("errors", errors);
            
            result.setCode(1);
            result.setData(JSON.toJSONString(data));
            return result;
        } catch (Exception e) {
            result.setCode(0);
            result.setData("文件读取失败: " + e.getMessage());
            return result;
        }
    }

    @Override
    public Result importAdmins(MultipartFile file) {
        Result result = new Result();
        try {
            List<Admin> admins = new ArrayList<>();
            List<String> errors = new ArrayList<>();
            
            BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8));
            String line;
            int lineNumber = 0;
            
            while ((line = reader.readLine()) != null) {
                lineNumber++;
                if (lineNumber == 1) continue; // 跳过表头
                
                if (line.trim().isEmpty()) continue;
                
                String[] fields = parseCSVLine(line);
                
                try {
                    Admin admin = new Admin();
                    admin.setUsername(fields.length > 0 ? fields[0].trim() : null);
                    admin.setPassword(fields.length > 1 ? fields[1].trim() : null);
                    admin.setName(fields.length > 2 ? fields[2].trim() : null);
                    admin.setRole(fields.length > 3 && !fields[3].trim().isEmpty() ? fields[3].trim() : "operator");
                    
                    String siteIdStr = fields.length > 4 ? fields[4].trim() : null;
                    if (siteIdStr != null && !siteIdStr.isEmpty()) {
                        try {
                            admin.setSite_id(Long.parseLong(siteIdStr));
                        } catch (NumberFormatException e) {
                            admin.setSite_id(null);
                        }
                    }
                    
                    String statusStr = fields.length > 5 ? fields[5].trim() : null;
                    if (statusStr != null && !statusStr.isEmpty()) {
                        try {
                            admin.setStatus(Integer.parseInt(statusStr));
                        } catch (NumberFormatException e) {
                            admin.setStatus(1);
                        }
                    } else {
                        admin.setStatus(1);
                    }
                    
                    if (admin.getUsername() == null || admin.getUsername().isEmpty()) {
                        errors.add("第" + lineNumber + "行: 账号不能为空");
                        continue;
                    }
                    if (admin.getPassword() == null || admin.getPassword().isEmpty()) {
                        errors.add("第" + lineNumber + "行: 密码不能为空");
                        continue;
                    }
                    if (!Arrays.asList("super", "admin", "operator").contains(admin.getRole())) {
                        errors.add("第" + lineNumber + "行: 角色只能是 super、admin 或 operator");
                        continue;
                    }
                    
                    Admin existAdmin = adminMapper.findByUsername(admin.getUsername());
                    if (existAdmin != null) {
                        errors.add("第" + lineNumber + "行: 账号 '" + admin.getUsername() + "' 已存在");
                        continue;
                    }
                    
                    admin.setPassword(passwordEncoder.encode(admin.getPassword()));
                    admins.add(admin);
                } catch (Exception e) {
                    errors.add("第" + lineNumber + "行: " + e.getMessage());
                }
            }
            
            reader.close();
            
            int successCount = 0;
            for (Admin admin : admins) {
                try {
                    adminMapper.insert(admin);
                    successCount++;
                } catch (Exception e) {
                    errors.add("管理员 '" + admin.getUsername() + "' 导入失败: " + e.getMessage());
                }
            }
            
            Map<String, Object> data = new HashMap<>();
            data.put("success", successCount);
            data.put("failed", errors.size());
            data.put("errors", errors);
            
            result.setCode(1);
            result.setData(JSON.toJSONString(data));
            return result;
        } catch (Exception e) {
            result.setCode(0);
            result.setData("文件读取失败: " + e.getMessage());
            return result;
        }
    }

    @Override
    public Result getSiteTemplate() {
        Result result = new Result();
        result.setCode(1);
        result.setData("景点导入模板: 景点名称,描述,评分,景区评分,浏览量,图标");
        return result;
    }

    @Override
    public Result getAdminTemplate() {
        Result result = new Result();
        result.setCode(1);
        result.setData("员工导入模板: 账号,密码,姓名,角色,所属景点ID,状态");
        return result;
    }

    private String[] parseCSVLine(String line) {
        List<String> fields = new ArrayList<>();
        StringBuilder field = new StringBuilder();
        boolean inQuotes = false;
        
        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);
            
            if (c == '"') {
                if (inQuotes && i + 1 < line.length() && line.charAt(i + 1) == '"') {
                    field.append('"');
                    i++;
                } else {
                    inQuotes = !inQuotes;
                }
            } else if (c == ',' && !inQuotes) {
                fields.add(field.toString());
                field = new StringBuilder();
            } else {
                field.append(c);
            }
        }
        fields.add(field.toString());
        
        return fields.toArray(new String[0]);
    }

    private float parseFloat(String value, float defaultValue) {
        try {
            return Float.parseFloat(value);
        } catch (Exception e) {
            return defaultValue;
        }
    }

    private int parseInt(String value, int defaultValue) {
        try {
            return Integer.parseInt(value);
        } catch (Exception e) {
            return defaultValue;
        }
    }

    @Override
    public void exportOrders(HttpServletResponse response, String status) {
        try {
            List<Map<String, Object>> orders = orderMapper.findAllOrders();
            
            if (status != null && !status.isEmpty()) {
                orders = orders.stream()
                    .filter(o -> status.equals(o.get("status")))
                    .toList();
            }

            response.setContentType("text/csv;charset=UTF-8");
            response.setHeader("Content-Disposition", "attachment;filename=orders_" + System.currentTimeMillis() + ".csv");

            OutputStreamWriter writer = new OutputStreamWriter(response.getOutputStream(), StandardCharsets.UTF_8);
            writer.write("\uFEFF");
            writer.write("订单号,用户,金额,状态,支付方式,创建时间,支付时间\n");

            for (Map<String, Object> order : orders) {
                String orderNo = String.valueOf(order.get("order_no") != null ? order.get("order_no") : order.get("order_id"));
                String username = order.get("username") != null ? String.valueOf(order.get("username")) : "";
                String price = order.get("price") != null ? String.valueOf(order.get("price")) : "0";
                String orderStatus = getStatusText(String.valueOf(order.get("status")));
                String paymentMethod = order.get("payment_method") != null ? String.valueOf(order.get("payment_method")) : "";
                String createdAt = order.get("created_at") != null ? String.valueOf(order.get("created_at")) : "";
                String paymentTime = order.get("payment_time") != null ? String.valueOf(order.get("payment_time")) : "";

                writer.write(escapeCSV(orderNo) + ",");
                writer.write(escapeCSV(username) + ",");
                writer.write(escapeCSV(price) + ",");
                writer.write(escapeCSV(orderStatus) + ",");
                writer.write(escapeCSV(paymentMethod) + ",");
                writer.write(escapeCSV(createdAt) + ",");
                writer.write(escapeCSV(paymentTime) + "\n");
            }

            writer.flush();
            writer.close();
        } catch (Exception e) {
            throw new RuntimeException("导出失败: " + e.getMessage());
        }
    }

    @Override
    public void exportAgriOrders(HttpServletResponse response, String status) {
        try {
            List<Map<String, Object>> orders = agriOrderMapper.findAll();

            if (status != null && !status.isEmpty()) {
                orders = orders.stream()
                    .filter(o -> status.equals(o.get("status")))
                    .toList();
            }

            response.setContentType("text/csv;charset=UTF-8");
            response.setHeader("Content-Disposition", "attachment;filename=agri_orders_" + System.currentTimeMillis() + ".csv");

            OutputStreamWriter writer = new OutputStreamWriter(response.getOutputStream(), StandardCharsets.UTF_8);
            writer.write("\uFEFF");
            writer.write("订单号,用户,产品名称,数量(斤),单价(元/斤),总价(元),状态,创建时间,支付时间\n");

            for (Map<String, Object> order : orders) {
                String orderNo = order.get("order_no") != null ? String.valueOf(order.get("order_no")) : "";
                String username = order.get("username") != null ? String.valueOf(order.get("username")) : "";
                String productName = order.get("product_name") != null ? String.valueOf(order.get("product_name")) : "";
                String quantity = order.get("quantity_jin") != null ? String.valueOf(order.get("quantity_jin")) : "0";
                String pricePerJin = order.get("price_per_jin") != null ? String.valueOf(order.get("price_per_jin")) : "0";
                String totalPrice = order.get("total_price") != null ? String.valueOf(order.get("total_price")) : "0";
                String orderStatus = getAgriStatusText(String.valueOf(order.get("status")));
                String createdAt = order.get("create_time") != null ? String.valueOf(order.get("create_time")) : "";
                String paymentTime = order.get("pay_time") != null ? String.valueOf(order.get("pay_time")) : "";

                writer.write(escapeCSV(orderNo) + ",");
                writer.write(escapeCSV(username) + ",");
                writer.write(escapeCSV(productName) + ",");
                writer.write(escapeCSV(quantity) + ",");
                writer.write(escapeCSV(pricePerJin) + ",");
                writer.write(escapeCSV(totalPrice) + ",");
                writer.write(escapeCSV(orderStatus) + ",");
                writer.write(escapeCSV(createdAt) + ",");
                writer.write(escapeCSV(paymentTime) + "\n");
            }

            writer.flush();
            writer.close();
        } catch (Exception e) {
            throw new RuntimeException("导出失败: " + e.getMessage());
        }
    }

    private String escapeCSV(String value) {
        if (value == null) return "";
        if (value.contains(",") || value.contains("\"") || value.contains("\n")) {
            return "\"" + value.replace("\"", "\"\"") + "\"";
        }
        return value;
    }

    private String getStatusText(String status) {
        if (status == null) return "未知";
        switch (status) {
            case "pending": return "待支付";
            case "confirmed": return "已确认";
            case "completed": return "已完成";
            case "cancelled": return "已取消";
            default: return status;
        }
    }

    private String getAgriStatusText(String status) {
        if (status == null) return "未知";
        switch (status) {
            case "pending": return "待支付";
            case "confirmed": return "已确认";
            case "completed": return "已完成";
            case "cancelled": return "已取消";
            default: return status;
        }
    }
}
