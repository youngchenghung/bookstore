package com.example.bookstore.service;

import com.example.bookstore.dto.MemberLoginRequest;
import com.example.bookstore.dto.MemberRegisterRequest;
import com.example.bookstore.model.Member;

public interface MemberService {

    // Integer register(MemberRegisterRequest memberRegisterRequest);

    // Security 註冊會員
    Integer register(Member member);

    // 更新會員Email
    void updateEmail(Integer memberId, String newEmail);
}
