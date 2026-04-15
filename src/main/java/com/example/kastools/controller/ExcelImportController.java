package com.example.kastools.controller;

import com.example.kastools.entity.Result;
import com.example.kastools.service.AdminPermissionService;
import com.example.kastools.service.ExcelImportService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/admin/excel")
public class ExcelImportController {

    @Autowired
    private ExcelImportService excelImportService;

    @Autowired
    private AdminPermissionService adminPermissionService;

    @PostMapping("/import/sites")
    public Result importSites(@RequestParam("file") MultipartFile file, HttpServletRequest request) {
        AdminPermissionService.AdminInfo adminInfo = adminPermissionService.getCurrentAdminInfo(request);
        if (adminInfo == null) {
            Result result = new Result();
            result.setCode(0);
            result.setData("请先登录");
            return result;
        }
        if (!adminPermissionService.isAdmin(adminInfo.getAdmin())) {
            Result result = new Result();
            result.setCode(0);
            result.setData("没有权限执行此操作");
            return result;
        }
        return excelImportService.importSites(file);
    }

    @PostMapping("/import/admins")
    public Result importAdmins(@RequestParam("file") MultipartFile file, HttpServletRequest request) {
        AdminPermissionService.AdminInfo adminInfo = adminPermissionService.getCurrentAdminInfo(request);
        if (adminInfo == null) {
            Result result = new Result();
            result.setCode(0);
            result.setData("请先登录");
            return result;
        }
        if (!adminPermissionService.isAdmin(adminInfo.getAdmin())) {
            Result result = new Result();
            result.setCode(0);
            result.setData("没有权限执行此操作");
            return result;
        }
        return excelImportService.importAdmins(file);
    }

    @GetMapping("/template/sites")
    public Result getSiteTemplate() {
        return excelImportService.getSiteTemplate();
    }

    @GetMapping("/template/admins")
    public Result getAdminTemplate() {
        return excelImportService.getAdminTemplate();
    }

    @GetMapping("/export/orders")
    public void exportOrders(@RequestParam(required = false) String status, HttpServletRequest request, HttpServletResponse response) {
        AdminPermissionService.AdminInfo adminInfo = adminPermissionService.getCurrentAdminInfo(request);
        if (adminInfo == null || !adminPermissionService.isAdmin(adminInfo.getAdmin())) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }
        excelImportService.exportOrders(response, status);
    }

    @GetMapping("/export/agri-orders")
    public void exportAgriOrders(@RequestParam(required = false) String status, HttpServletRequest request, HttpServletResponse response) {
        AdminPermissionService.AdminInfo adminInfo = adminPermissionService.getCurrentAdminInfo(request);
        if (adminInfo == null || !adminPermissionService.isAdmin(adminInfo.getAdmin())) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }
        excelImportService.exportAgriOrders(response, status);
    }
}
