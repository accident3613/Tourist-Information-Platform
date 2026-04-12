package com.example.kastools.controller;

import com.example.kastools.entity.Result;
import com.example.kastools.entity.User;
import com.example.kastools.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController()
@RequestMapping("/user")
public class UserC {

    @Autowired
    private UserService userService;

    @PostMapping(value = "/login")
    public Result login(@RequestBody User user, HttpServletRequest request) {
        return userService.login(user, request);
    }

    @PostMapping(value = "/logout")
    public Boolean logout(HttpServletRequest request) {
        return userService.logout(request);
    }

    @PostMapping("/register")
    public Result register(@RequestBody User user) {
        return userService.register(user);
    }

    @GetMapping("/profile")
    public User profile(HttpServletRequest request) {
        return userService.profile(request);
    }

    @PutMapping("/update-name")
    public Result name(String name, HttpServletRequest request) throws InterruptedException {
        return userService.updateName(name, request);
    }

    @PostMapping("/colist")
    public List<Integer> colist(HttpServletRequest request) {
        return userService.getCollectionList(request);
    }

    @GetMapping("/coladd")
    public boolean coladd(HttpServletRequest request, @RequestParam("site_id") int site_id) {
        return userService.addCollection(request, site_id);
    }

    @GetMapping("/coldel")
    public boolean coldel(HttpServletRequest request, @RequestParam("site_id") int site_id) {
        return userService.deleteCollection(request, site_id);
    }
}
