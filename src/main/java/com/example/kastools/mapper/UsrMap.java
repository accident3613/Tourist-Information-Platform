package com.example.kastools.mapper;

import com.example.kastools.entity.User;
import org.apache.ibatis.annotations.*;


@Mapper
public interface UsrMap {
    @Select("SELECT COUNT(*) FROM user WHERE username = #{username} AND password = #{password}")
    int logmap(String username, String password);  //登录
@Insert("INSERT INTO user (username, password) VALUES (#{username}, #{password})")
    int regmap(String username, String password);  //注册

@Select("SELECT * FROM user WHERE username = #{username}")
    User prfmap(String username);  //获取用户信息

    @Update("UPDATE user SET name = #{name} WHERE username = #{username}")
    boolean upnmap(String name,String username);
    @Update("UPDATE user SET icon = #{icon} WHERE username = #{username}")
    boolean upimap(String icon,String username);
}
