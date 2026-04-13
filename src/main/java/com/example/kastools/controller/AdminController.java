package com.example.kastools.controller;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.example.kastools.entity.Admin;
import com.example.kastools.entity.Result;
import com.example.kastools.service.AdminService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private AdminService adminService;

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
        Result result = adminService.createAdmin(admin);
        // 记录日志
        return result;
    }

    @PutMapping("/update")
    public Result updateAdmin(@RequestBody Admin admin) {
        return adminService.updateAdmin(admin);
    }

    @DeleteMapping("/delete/{id}")
    public Result deleteAdmin(@PathVariable Long id) {
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
}
