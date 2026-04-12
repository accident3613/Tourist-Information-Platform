package com.example.kastools.service;

import com.example.kastools.entity.Result;
import com.example.kastools.entity.User;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;

public interface UserService {

    Result login(User user, HttpServletRequest request);

    Boolean logout(HttpServletRequest request);

    Result register(User user);

    User profile(HttpServletRequest request);

    Result updateName(String name, HttpServletRequest request) throws InterruptedException;

    List<Integer> getCollectionList(HttpServletRequest request);

    boolean addCollection(HttpServletRequest request, int siteId);

    boolean deleteCollection(HttpServletRequest request, int siteId);
}
