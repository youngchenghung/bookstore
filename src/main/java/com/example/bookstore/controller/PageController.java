package com.example.bookstore.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
public class PageController {

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
}