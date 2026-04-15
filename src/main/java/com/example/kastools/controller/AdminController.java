package com.example.kastools.controller;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.example.kastools.entity.Admin;
import com.example.kastools.entity.Result;
import com.example.kastools.entity.Site_list;
import com.example.kastools.service.AdminService;
import com.example.kastools.service.AdminPermissionService;
import com.example.kastools.service.OrderService;
import com.example.kastools.service.UserService;
import com.example.kastools.service.SiteService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private AdminService adminService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private UserService userService;

    @Autowired
    private SiteService siteService;

    @Autowired
    private AdminPermissionService adminPermissionService;

    @PostMapping("/login")
    public Result login(@RequestBody String body) {
        JSONObject json = JSON.parseObject(body);
        String username = json.getString("username");
        String password = json.getString("password");
        return adminService.login(username, password);
    }

    @PostMapping("/logout")
    public Result logout(@RequestHeader(value = "token", required = false) String token) {
        return adminService.logout(token);
    }

    @GetMapping("/profile")
    public Result getProfile(@RequestHeader(value = "token", required = false) String token) {
        return adminService.getProfile(token);
    }

    @GetMapping("/list")
    public Result getAdminList() {
        return adminService.getAdminList();
    }

    @PostMapping("/create")
    public Result createAdmin(@RequestBody Admin admin, HttpServletRequest request) {
        // 检查是否有管理员权限（只有超级管理员和管理员可以创建管理员）
        Admin currentAdmin = adminPermissionService.getCurrentAdmin(request);
        if (currentAdmin == null) {
            Result result = new Result();
            result.setCode(0);
            result.setData("请先登录");
            return result;
        }
        if (!adminPermissionService.isAdmin(currentAdmin)) {
            Result result = new Result();
            result.setCode(0);
            result.setData("没有权限执行此操作");
            return result;
        }
        // 运营人员不能创建管理员
        if (adminPermissionService.isOperator(currentAdmin)) {
            Result result = new Result();
            result.setCode(0);
            result.setData("运营人员没有权限创建管理员");
            return result;
        }
        Result result = adminService.createAdmin(admin);
        return result;
    }

    @PutMapping("/update")
    public Result updateAdmin(@RequestBody Admin admin, HttpServletRequest request) {
        // 检查是否有管理员权限
        Admin currentAdmin = adminPermissionService.getCurrentAdmin(request);
        if (currentAdmin == null) {
            Result result = new Result();
            result.setCode(0);
            result.setData("请先登录");
            return result;
        }
        if (!adminPermissionService.isAdmin(currentAdmin)) {
            Result result = new Result();
            result.setCode(0);
            result.setData("没有权限执行此操作");
            return result;
        }
        // 运营人员不能修改管理员
        if (adminPermissionService.isOperator(currentAdmin)) {
            Result result = new Result();
            result.setCode(0);
            result.setData("运营人员没有权限修改管理员");
            return result;
        }
        return adminService.updateAdmin(admin);
    }

    @DeleteMapping("/delete/{id}")
    public Result deleteAdmin(@PathVariable Long id, HttpServletRequest request) {
        // 检查是否有管理员权限
        Admin currentAdmin = adminPermissionService.getCurrentAdmin(request);
        if (currentAdmin == null) {
            Result result = new Result();
            result.setCode(0);
            result.setData("请先登录");
            return result;
        }
        if (!adminPermissionService.isAdmin(currentAdmin)) {
            Result result = new Result();
            result.setCode(0);
            result.setData("没有权限执行此操作");
            return result;
        }
        // 运营人员不能删除管理员
        if (adminPermissionService.isOperator(currentAdmin)) {
            Result result = new Result();
            result.setCode(0);
            result.setData("运营人员没有权限删除管理员");
            return result;
        }
        return adminService.deleteAdmin(id);
    }

    @PutMapping("/reset-password/{id}")
    public Result resetPassword(@PathVariable Long id, @RequestBody String body) {
        JSONObject json = JSON.parseObject(body);
        String newPassword = json.getString("newPassword");
        return adminService.resetPassword(id, newPassword);
    }

    @GetMapping("/logs")
    public Result getLogs(@RequestParam(defaultValue = "50") int limit) {
        return adminService.getLogs(limit);
    }

    @GetMapping("/orders")
    public Result getAllOrders() {
        Result result = new Result();
        List<Map<String, Object>> orders = orderService.getAllOrders();
        result.setCode(1);
        result.setData(JSON.toJSONString(orders));
        return result;
    }

    @GetMapping("/users")
    public Result getAllUsers() {
        Result result = new Result();
        List<?> users = userService.getAllUsers();
        result.setCode(1);
        result.setData(JSON.toJSONString(users));
        return result;
    }

    @GetMapping("/stats")
    public Result getStats() {
        return adminService.getStats();
    }

    @PutMapping("/user/status")
    public Result toggleUserStatus(@RequestBody String body) {
        JSONObject json = JSON.parseObject(body);
        String username = json.getString("username");
        Integer status = json.getInteger("status");
        return userService.toggleUserStatus(username, status);
    }

    @GetMapping("/sites/all")
    public Result getAllSites() {
        Result result = new Result();
        List<Site_list> sites = siteService.getAllSites();
        result.setCode(1);
        result.setData(JSON.toJSONString(sites));
        return result;
    }
}
