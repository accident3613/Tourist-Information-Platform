package com.example.kastools.service;

import com.example.kastools.entity.Admin;
import com.example.kastools.entity.Result;
import com.example.kastools.mapper.AdminMapper;
import com.example.kastools.utils.Jwt;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AdminPermissionService {

    @Autowired
    private AdminMapper adminMapper;

    @Autowired
    private Jwt jwt;

    public static class AdminInfo {
        private Admin admin;
        private String token;

        public AdminInfo(Admin admin, String token) {
            this.admin = admin;
            this.token = token;
        }

        public Admin getAdmin() {
            return admin;
        }

        public String getToken() {
            return token;
        }
    }

    /**
     * 获取当前登录管理员
     */
    public Admin getCurrentAdmin(HttpServletRequest request) {
        String token = request.getHeader("token");
        String username = jwt.getusn(token);
        if (username == null) {
            return null;
        }
        return adminMapper.findByUsername(username);
    }

    /**
     * 获取当前登录管理员信息（包含token）
     */
    public AdminInfo getCurrentAdminInfo(HttpServletRequest request) {
        String token = request.getHeader("token");
        String username = jwt.getusn(token);
        if (username == null) {
            return null;
        }
        Admin admin = adminMapper.findByUsername(username);
        if (admin == null) {
            return null;
        }
        return new AdminInfo(admin, token);
    }

    /**
     * 检查是否是超级管理员
     */
    public boolean isSuperAdmin(Admin admin) {
        return admin != null && "super".equals(admin.getRole());
    }

    /**
     * 检查是否是管理员（包括超级管理员）
     */
    public boolean isAdmin(Admin admin) {
        return admin != null && ("super".equals(admin.getRole()) || "admin".equals(admin.getRole()));
    }

    /**
     * 检查是否是运营人员
     */
    public boolean isOperator(Admin admin) {
        return admin != null && "operator".equals(admin.getRole());
    }

    /**
     * 检查运营人员是否有权限操作该景点
     */
    public boolean hasSitePermission(Admin admin, Long siteId) {
        if (admin == null || siteId == null) {
            return false;
        }
        // 超级管理员和管理员可以操作所有景点
        if (isAdmin(admin)) {
            return true;
        }
        // 运营人员只能操作自己所属的景点
        if (isOperator(admin)) {
            return siteId.equals(admin.getSite_id());
        }
        return false;
    }

    /**
     * 检查是否有景点管理权限
     */
    public Result checkSitePermission(HttpServletRequest request, Long siteId) {
        Result result = new Result();
        Admin admin = getCurrentAdmin(request);
        
        if (admin == null) {
            result.setCode(0);
            result.setData("请先登录");
            return result;
        }

        if (!hasSitePermission(admin, siteId)) {
            result.setCode(0);
            result.setData("没有权限操作该景点");
            return result;
        }

        result.setCode(1);
        result.setData(String.valueOf(admin));
        return result;
    }

    /**
     * 检查是否有管理员权限（非运营人员）
     */
    public Result checkAdminPermission(HttpServletRequest request) {
        Result result = new Result();
        Admin admin = getCurrentAdmin(request);
        
        if (admin == null) {
            result.setCode(0);
            result.setData("请先登录");
            return result;
        }

        if (!isAdmin(admin)) {
            result.setCode(0);
            result.setData("没有权限执行此操作");
            return result;
        }

        result.setCode(1);
        result.setData(String.valueOf(admin));
        return result;
    }

    /**
     * 检查是否有超级管理员权限
     */
    public Result checkSuperAdminPermission(HttpServletRequest request) {
        Result result = new Result();
        Admin admin = getCurrentAdmin(request);
        
        if (admin == null) {
            result.setCode(0);
            result.setData("请先登录");
            return result;
        }

        if (!isSuperAdmin(admin)) {
            result.setCode(0);
            result.setData("只有超级管理员可以执行此操作");
            return result;
        }

        result.setCode(1);
        result.setData(String.valueOf(admin));
        return result;
    }
}
