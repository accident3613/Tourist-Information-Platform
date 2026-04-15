package com.example.kastools.service;

import com.example.kastools.entity.Admin;
import com.example.kastools.entity.AdminLog;
import com.example.kastools.entity.Result;

import java.util.List;

public interface AdminService {
    Result login(String username, String password);
    Result logout(String token);
    Result getProfile(String token);
    Result getAdminList();
    Result createAdmin(Admin admin);
    Result updateAdmin(Admin admin);
    Result deleteAdmin(Long id);
    Result resetPassword(Long id, String newPassword);
    Result getLogs(int limit);
    void addLog(Long adminId, String adminUsername, String action, String targetType, String targetId, String detail, String ip);
    Result getStats();
}
