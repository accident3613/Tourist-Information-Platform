package com.example.kastools.mapper;

import com.example.kastools.entity.Admin;
import com.example.kastools.entity.AdminLog;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface AdminMapper {

    @Select("SELECT * FROM admin WHERE username = #{username}")
    Admin findByUsername(String username);

    @Select("SELECT * FROM admin WHERE id = #{id}")
    Admin findById(Long id);

    @Select("SELECT * FROM admin ORDER BY create_time DESC")
    List<Admin> findAll();

    @Insert("INSERT INTO admin (username, password, name, role, status) " +
            "VALUES (#{username}, #{password}, #{name}, #{role}, #{status})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(Admin admin);

    @Update("UPDATE admin SET name = #{name}, role = #{role}, status = #{status} " +
            "WHERE id = #{id}")
    int update(Admin admin);

    @Update("UPDATE admin SET password = #{password} WHERE id = #{id}")
    int updatePassword(@Param("id") Long id, @Param("password") String password);

    @Update("UPDATE admin SET last_login_time = NOW() WHERE id = #{id}")
    int updateLastLoginTime(Long id);

    @Delete("DELETE FROM admin WHERE id = #{id}")
    int deleteById(Long id);

    @Insert("INSERT INTO admin_log (admin_id, admin_username, action, target_type, target_id, detail, ip) " +
            "VALUES (#{adminId}, #{adminUsername}, #{action}, #{targetType}, #{targetId}, #{detail}, #{ip})")
    int insertLog(AdminLog log);

    @Select("SELECT * FROM admin_log ORDER BY create_time DESC LIMIT #{limit}")
    List<AdminLog> findRecentLogs(@Param("limit") int limit);

    @Select("SELECT COUNT(*) FROM admin WHERE status = 1")
    int countActiveAdmins();
}
