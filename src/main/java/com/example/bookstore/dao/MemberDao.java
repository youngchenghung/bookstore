package com.example.bookstore.dao;

import com.example.bookstore.dto.MemberLoginRequest;
import com.example.bookstore.dto.MemberRegisterRequest;
import com.example.bookstore.model.Member;

public interface MemberDao {

    Integer register(MemberRegisterRequest memberRegisterRequest);

    Member getMemberByEmail(String Email);

    Member getMemberById(Integer memberId);

    Member memberLogin(String email, String password);
}
