package com.example.bookstore.controller;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.example.bookstore.dao.MemberDao;
import com.example.bookstore.dto.MemberLoginRequest;
import com.example.bookstore.dto.MemberRegisterRequest;
import com.example.bookstore.model.Member;
import com.example.bookstore.service.MemberService;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@Valid
public class MemberController {

    private final static Logger log = LoggerFactory.getLogger(MemberController.class);

    @Autowired
    private MemberDao memberDao;

    @Autowired
    private MemberService memberService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // // 註冊會員
    // @PostMapping("/register")
    // public ResponseEntity<Integer> register(@RequestBody @Valid MemberRegisterRequest memberRegisterRequest) {

    //     // 檢查是否已註冊過
    //     Member member = memberDao.getMemberByEmail(memberRegisterRequest.getEmail());

    //     if (member != null) {
    //         log.warn("註冊失敗: {} 用戶已存在" , memberRegisterRequest.getEmail());
    //         return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();

    //     } 
        
    //     // 若未註冊過，則註冊
    //     else {
    //         log.info("註冊會員: {}", memberRegisterRequest.getEmail());
    //         Integer memberId = memberDao.register(memberRegisterRequest);

    //         log.info("註冊成功, 會員名稱: {}, 會員信箱: {}", memberRegisterRequest.getMemberName(), memberRegisterRequest.getEmail());
    //         return ResponseEntity.status(HttpStatus.CREATED).body(memberId);
    //     }
    // }

    // // 會員登入
    // @PostMapping("/user/login")
    // public ResponseEntity<Member> login(@RequestBody @Valid MemberLoginRequest memberLoginRequest) {

    //     Member memberLogin = memberDao.memberLogin(memberLoginRequest.getEmail(), memberLoginRequest.getPassword());

    //     // 若查無此會員，回傳 404 Not Found
    //     if (memberLogin == null) {
    //         log.warn("登入失敗: {} 帳號密碼輸入錯誤", memberLoginRequest.getEmail());
    //         return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    //     }

    //     // 若查詢成功，回傳 200 OK
    //     else {
    //         log.info("登入成功: {}", memberLoginRequest.getEmail());
    //         return ResponseEntity.status(HttpStatus.OK).body(memberLogin);
    //     }
    // }

    // 註冊使用者帳號
    @PostMapping("/register")
    public ResponseEntity<Integer> register(@RequestBody Member member) {

        // 將密碼進行加密
        String hashedPassword = passwordEncoder.encode(member.getPassword());
        // 將加密後的密碼設定回使用者物件
        member.setPassword(hashedPassword);

        Member memberCheck = memberDao.getMemberByEmail(member.getEmail());

        // 若註冊Email已存在，回傳 400 Bad Request
        if (memberCheck != null) {
            log.warn("註冊失敗: {} Email已使用", member.getEmail());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        else {
            log.info("註冊會員: {}", member.getEmail());

            // 在資料庫中新增使用者資料
            Integer memberId = memberDao.register(member);

            return ResponseEntity.status(HttpStatus.CREATED).body(memberId);
        }
    }

    // 使用者權限
    @GetMapping("/{memberId}/privilege")
    public ResponseEntity<?> hello(@PathVariable Integer memberId, Authentication authentication) {

        String username = authentication.getName();

        if (username == null) {
            // 若未登入，回傳 401 Unauthorized
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        Collection<? extends GrantedAuthority> authenticaties = authentication.getAuthorities();

        return ResponseEntity.status(HttpStatus.OK).body(authenticaties);
    }

    // 使用者資訊
    @GetMapping("/{memberId}/userInfo")
    public ResponseEntity<Member> userInfo(@PathVariable Integer memberId, Authentication authentication) {
        
        // 驗證登入的使用者 ID 是否與更新的 memberId 相符
        String authenticatedEmail = authentication.getName();
        Member authenticatedMember = memberDao.getMemberByEmail(authenticatedEmail);

        // 防止更新其他使用者的資料
        if (!authenticatedMember.getMemberId().equals(memberId)) {
            // 若非本人，回傳 403 Forbidden
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        Member member = memberDao.getMemberById(memberId);
        // 若查無此會員，回傳 404 Not Found        
        if (member == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        return ResponseEntity.status(HttpStatus.OK).body(member);
    }

    // 更新會員Email
    @PutMapping("/{memberId}/updateEmail")
    public ResponseEntity<?> userUpdateEmail(@PathVariable Integer memberId, @RequestParam String newEmail, Authentication authentication) {

        // 驗證登入的使用者 ID 是否與更新的 memberId 相符
        String authenticatedEmail = authentication.getName();
        Member authenticatedMember = memberDao.getMemberByEmail(authenticatedEmail);
        
        // 防止更新其他使用者的資料
        if (!authenticatedMember.getMemberId().equals(memberId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        // 更新會員Email
        memberService.updateEmail(memberId, newEmail);

        return ResponseEntity.status(HttpStatus.OK).build();
    }
    
}


