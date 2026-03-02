package com.example.kastools.mapper;

import com.example.kastools.entity.User;
import org.apache.ibatis.annotations.*;


@Mapper
public interface UsrMap {
    @Select("SELECT COUNT(*) FROM user WHERE username = #{username} AND password = #{password}")
    int logmap(String username, String password);
@Insert("INSERT INTO user (username, password) VALUES (#{username}, #{password})")
    int regmap(String username, String password);

@Select("SELECT * FROM user WHERE username = #{username}")
    User prfmap(String username);


}
