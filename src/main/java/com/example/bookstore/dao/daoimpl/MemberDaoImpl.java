package com.example.bookstore.dao.daoimpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import com.example.bookstore.dao.MemberDao;
import com.example.bookstore.dto.MemberLoginRequest;
import com.example.bookstore.dto.MemberRegisterRequest;
import com.example.bookstore.model.Member;
import com.example.bookstore.rowmapper.MemberRowMapper;

import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;

import java.util.HashMap;
import java.util.Map;
import java.util.List;

@Component
public class MemberDaoImpl implements MemberDao {
    
    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    // 註冊會員
    @Override
    public Integer register(MemberRegisterRequest memberRegisterRequest) {
        String sql = "INSERT INTO member (member_name, email, password) VALUES (:memberName, :email, :password)";

        Map<String, Object> map = new HashMap<>();
        map.put("memberName", memberRegisterRequest.getMemberName());
        map.put("email", memberRegisterRequest.getEmail());
        map.put("password", memberRegisterRequest.getPassword());

        KeyHolder KeyHolder = new GeneratedKeyHolder();

        namedParameterJdbcTemplate.update(sql, new MapSqlParameterSource(map), KeyHolder);

        Integer memberId = KeyHolder.getKey().intValue();

        return memberId;
    }

    // 透過會員 ID 取得會員
    @Override
    public Member getMemberById(Integer memberId) {
        String sql = "SELECT member_id, member_name, email, password FROM member WHERE member_id = :member_id";

        Map<String, Object> map = new HashMap<>();
        map.put("member_id", memberId);

        List<Member> member = namedParameterJdbcTemplate.query(sql, map, new MemberRowMapper());

        if (member.size() > 0) {
            return member.get(0);
        }

        return null;
    }

    // 透過 Email 取得會員
    @Override
    public Member getMemberByEmail(String Email) {
        String sql = "SELECT member_id, member_name, email, password FROM member WHERE email = :email";

        Map<String, Object> map = new HashMap<>();
        map.put("email", Email);

        List<Member> member = namedParameterJdbcTemplate.query(sql, map, new MemberRowMapper());

        if (member.size() > 0) {
            return member.get(0);
        }

        return null;

    }

    // 會員登入
    @Override
    public Member memberLogin(String email, String password) {
        String sql = "SELECT member_id, member_name, email, password FROM member WHERE email = :email AND password = :password";

        Map<String, Object> map = new HashMap<>();
        map.put("email", email);
        map.put("password", password);

        List<Member> member = namedParameterJdbcTemplate.query(sql, map, new MemberRowMapper());

        if (member.size() > 0) {
            return member.get(0);
        }

        return null;
    }

}
