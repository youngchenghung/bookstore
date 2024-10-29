package com.example.bookstore.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.example.bookstore.dao.MemberDao;
import com.example.bookstore.dto.MemberLoginRequest;
import com.example.bookstore.dto.MemberRegisterRequest;
import com.example.bookstore.model.Member;

import jakarta.validation.Valid;

@RestController
@Valid
public class MemberController {

    private final static Logger log = LoggerFactory.getLogger(MemberController.class);

    @Autowired
    private MemberDao memberDao;

    // 註冊會員
    @PostMapping("/register")
    public ResponseEntity<Integer> register(@RequestBody @Valid MemberRegisterRequest memberRegisterRequest) {

        // 檢查是否已註冊過
        Member member = memberDao.getMemberByEmail(memberRegisterRequest.getEmail());

        if (member != null) {
            log.warn("註冊失敗: {} 用戶已存在" , memberRegisterRequest.getEmail());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();

        } 
        
        // 若未註冊過，則註冊
        else {
            log.info("註冊會員: {}", memberRegisterRequest.getEmail());
            Integer memberId = memberDao.register(memberRegisterRequest);

            log.info("註冊成功, 會員名稱: {}, 會員信箱: {}", memberRegisterRequest.getMemberName(), memberRegisterRequest.getEmail());
            return ResponseEntity.status(HttpStatus.CREATED).body(memberId);
        }
    }

    // 會員登入
    @PostMapping("/user/login")
    public ResponseEntity<Member> login(@RequestBody @Valid MemberLoginRequest memberLoginRequest) {

        Member memberLogin = memberDao.memberLogin(memberLoginRequest.getEmail(), memberLoginRequest.getPassword());

        // 若查無此會員，回傳 404 Not Found
        if (memberLogin == null) {
            log.warn("登入失敗: {} 帳號密碼輸入錯誤", memberLoginRequest.getEmail());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        // 若查詢成功，回傳 200 OK
        else {
            log.info("登入成功: {}", memberLoginRequest.getEmail());
            return ResponseEntity.status(HttpStatus.OK).body(memberLogin);
        }
    }
}
