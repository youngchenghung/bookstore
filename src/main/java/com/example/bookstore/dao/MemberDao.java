package com.example.bookstore.dao;

import com.example.bookstore.dto.MemberLoginRequest;
import com.example.bookstore.dto.MemberRegisterRequest;
import com.example.bookstore.model.Member;
import com.example.bookstore.model.Role;

import java.util.List;

public interface MemberDao {

    // Integer register(MemberRegisterRequest memberRegisterRequest);

    // Member getMemberByEmail(String Email);

    // Member getMemberById(Integer memberId);

    // Member memberLogin(String email, String password);

    Integer register(Member member);
    
    Member getMemberByEmail(String email);

    Member getMemberById(Integer memberId);

    List<Role> getRolesByMemberId(Integer memberId);

    void updateEmail(Integer memberId, String newEmail);

    Integer getMemberIdByEmail(String email);

}
