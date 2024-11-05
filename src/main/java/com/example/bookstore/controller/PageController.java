package com.example.bookstore.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;


@Controller
public class PageController {

    @GetMapping("/login")
    public String login() {
        return "login.html";
    }
    
    @GetMapping("/")
    public String home() {
        return "index.html"; // Spring Boot 會自動從 static 資料夾中尋找此文件
    }

    @GetMapping("/booklist")
    public String bookList() {
        return "booklist.html"; // Spring Boot 會自動從 static 資料夾中尋找此文件
    }
    
    @GetMapping("/member")
    public String member() {
        return "member.html"; // Spring Boot 會自動從 static 資料夾中尋找此文件
    }

    @GetMapping("/order")
    public String order() {
        return "order.html"; // Spring Boot 會自動從 static 資料夾中尋找此文件
    }

    @GetMapping("/{memberId}/profile.html")
    public String profilePage(@PathVariable("memberId") Integer memberId) {
        // 這裡的 memberId 是從 URL 中取得的，例如 /1/profile.html 中的 1
        return "forward:/profile.html";
    }

    // @GetMapping("/{memberId}/updateEmail")
    // public String updateEmail(@PathVariable Integer memberId) {
    //     return "updateEmail.html"; // Spring Boot 會自動從 static 資料夾中尋找此文件
    // }
}