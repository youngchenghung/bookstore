package com.example.bookstore.service.serviceimpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.example.bookstore.dao.MemberDao;
import com.example.bookstore.dto.MemberRegisterRequest;
import com.example.bookstore.model.Member;
import com.example.bookstore.service.MemberService;

@Component
public class MemberServiceImple implements MemberService {

    @Autowired MemberDao memberDao;

    // // 註冊會員
    // @Override
    // public Integer register(MemberRegisterRequest memberRegisterRequest) {
    //     return memberDao.register(memberRegisterRequest);
    // }

    // Security 註冊會員
    @Override
    public Integer register(Member member) {
        return memberDao.register(member);
    }

    @Override
    public void updateEmail(Integer memberId, String newEmail) {
        memberDao.updateEmail(memberId, newEmail);
    }
}
