package com.example.kastools.service;

import com.example.kastools.entity.Result;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.multipart.MultipartFile;

public interface ExcelImportService {
    Result importSites(MultipartFile file);
    Result importAdmins(MultipartFile file);
    Result getSiteTemplate();
    Result getAdminTemplate();
    void exportOrders(HttpServletResponse response, String status);
    void exportAgriOrders(HttpServletResponse response, String status);
}
