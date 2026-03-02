package com.example.kastools.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class Con {
    @RequestMapping("/")
    public String all()
    {
        return "redirect:/login.html";
    }
}
