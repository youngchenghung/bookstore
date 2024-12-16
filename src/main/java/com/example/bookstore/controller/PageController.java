package com.example.bookstore.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;


@Controller
public class PageController {

    @GetMapping("/login")
    public String loginPage() {
        return "forward:/login.html";
    }
    
    @GetMapping("/")
    public String home() {
        return "forward:/index.html"; // Spring Boot 會自動從 static 資料夾中尋找此文件
    }

    @GetMapping("/{memberId}/")
    public String homeLogged(@PathVariable("memberId") Integer memberId) {
        return "forward:/indexLogged.html";
    }

    @GetMapping("/{memberId}/bookList")
    public String bookList(@PathVariable("memberId") Integer memberId) {
        return "forward:/bookList.html"; // Spring Boot 會自動從 static 資料夾中尋找此文件
    }
    
    @GetMapping("/register")
    public String register() {
        return "register.html"; // Spring Boot 會自動從 static 資料夾中尋找此文件
    }

    @GetMapping("/{memberId}/order")
    public String order(@PathVariable("memberId") Integer memberId) {
        return "forward:/order.html"; // Spring Boot 會自動從 static 資料夾中尋找此文件
    }

    @GetMapping("/{memberId}/profile")
    public String profilePage(@PathVariable("memberId") Integer memberId) {
        return "forward:/profile.html";  // 假設 profile.html 在 static 資料夾下
    }

}