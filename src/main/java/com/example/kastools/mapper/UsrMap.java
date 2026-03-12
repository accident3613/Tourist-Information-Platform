package com.example.kastools.mapper;

import com.example.kastools.entity.Collection;
import com.example.kastools.entity.User;
import org.apache.ibatis.annotations.*;

import java.util.List;


@Mapper
public interface UsrMap {
    @Select("SELECT COUNT(*) FROM user WHERE username = #{username} AND password = #{password}")
    int logmap(String username, String password);  //登录
    @Update("UPDATE user SET status = #{status} WHERE username = #{username}")
    boolean lggmap(int status,String username);

@Insert("INSERT INTO user (username, password) VALUES (#{username}, #{password})")
    int regmap(String username, String password);  //注册

    @Select("SELECT status FROM user WHERE username = #{usernmae}")
    int chkmap(String username);  //检查登录状态
@Select("SELECT * FROM user WHERE username = #{username}")
    User prfmap(String username);  //获取用户信息

    @Update("UPDATE user SET name = #{name} WHERE username = #{username}")
    boolean upnmap(String name,String username);
    @Update("UPDATE user SET icon = #{icon} WHERE username = #{username}")
    boolean upimap(String icon,String username);

    @Insert("INSERT INTO collections (site_id,username) VALUES (#{site_id},#{username})")
    boolean colmap(int site_id,String username);


    @Select("SELECT site_id FROM collections WHERE username = #{username}")
    List<Integer> clsmap(String username);
}
