package com.example.bookstore.rowmapper;

import com.example.bookstore.model.Member;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class MemberRowMapper implements RowMapper<Member> {

    @Override
    public Member mapRow(ResultSet resultSet, int i) throws SQLException {

        Member member = new Member();

        member.setMemberId(resultSet.getInt("member_id"));
        member.setMemberName(resultSet.getString("member_name"));
        member.setEmail(resultSet.getString("email"));
        member.setPassword(resultSet.getString("password"));

        return member;
    }
}
