package com.example.bookstore.security;

import java.util.List;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import com.example.bookstore.model.Member;
import com.example.bookstore.model.Role;
import com.example.bookstore.dao.MemberDao;

@Component
public class MyUserDetailService implements UserDetailsService{

    @Autowired
    private MemberDao memberDao;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Member member = memberDao.getMemberByEmail(username);

        // 若用戶不存在，則拋出例外
        if (member == null) {
            throw new UsernameNotFoundException("用戶不存在 : " + username);
        }

        // 若用戶存在，則取得用戶的角色
        else {
            String memberEmail = member.getEmail();
            String memberPassword = member.getPassword();

            // 取得用戶的角色 (一個用戶可能有多個角色)
            List<Role> roleList = memberDao.getRolesByMemberId(member.getMemberId());

            // 將角色轉換為 authorities
            List<GrantedAuthority> authorities = convertToAuthorities(roleList);

            return new User(memberEmail, memberPassword, authorities);
        }
    }

    // authorities 列表
    private List<GrantedAuthority> convertToAuthorities(List<Role> roleList) {
        List<GrantedAuthority> authorities = new ArrayList<>();

        // 將角色轉加入 authorities 列表中
        // 例如: ROLE_ADMIN -> new SimpleGrantedAuthority("ROLE_ADMIN")
        // 例如: ROLE_USER -> new SimpleGrantedAuthority("ROLE_USER")
        for (Role role : roleList) {
            authorities.add(new SimpleGrantedAuthority(role.getRoleName()));
        }

        return authorities;
    }

}
