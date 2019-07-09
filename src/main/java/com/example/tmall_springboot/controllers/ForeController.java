package com.example.tmall_springboot.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ForeController {

    @GetMapping("/")
    public String index() {
        return "redirect:home";
    }
    @GetMapping("/home")
    public String home() {
        return "fore/home";
    }
}
