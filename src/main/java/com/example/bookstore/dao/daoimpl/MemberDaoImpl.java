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
import com.example.bookstore.model.Role;
import com.example.bookstore.rowmapper.MemberRowMapper;
import com.example.bookstore.rowmapper.RoleRowMapper;

import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;

import java.util.HashMap;
import java.util.Map;
import java.util.List;

@Component
public class MemberDaoImpl implements MemberDao {
    
    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    // // 註冊會員
    // @Override
    // public Integer register(MemberRegisterRequest memberRegisterRequest) {
    //     String sql = "INSERT INTO member (member_name, email, password) VALUES (:memberName, :email, :password)";

    //     Map<String, Object> map = new HashMap<>();
    //     map.put("memberName", memberRegisterRequest.getMemberName());
    //     map.put("email", memberRegisterRequest.getEmail());
    //     map.put("password", memberRegisterRequest.getPassword());

    //     KeyHolder KeyHolder = new GeneratedKeyHolder();

    //     namedParameterJdbcTemplate.update(sql, new MapSqlParameterSource(map), KeyHolder);

    //     Integer memberId = KeyHolder.getKey().intValue();

    //     return memberId;
    // }

    // // 透過會員 ID 取得會員
    // @Override
    // public Member getMemberById(Integer memberId) {
    //     String sql = "SELECT member_id, member_name, email, password FROM member WHERE member_id = :member_id";

    //     Map<String, Object> map = new HashMap<>();
    //     map.put("member_id", memberId);

    //     List<Member> member = namedParameterJdbcTemplate.query(sql, map, new MemberRowMapper());

    //     if (member.size() > 0) {
    //         return member.get(0);
    //     }

    //     return null;
    // }

    // // 透過 Email 取得會員
    // @Override
    // public Member getMemberByEmail(String Email) {
    //     String sql = "SELECT member_id, member_name, email, password FROM member WHERE email = :email";

    //     Map<String, Object> map = new HashMap<>();
    //     map.put("email", Email);

    //     List<Member> member = namedParameterJdbcTemplate.query(sql, map, new MemberRowMapper());

    //     if (member.size() > 0) {
    //         return member.get(0);
    //     }

    //     return null;

    // }

    // // 會員登入
    // @Override
    // public Member memberLogin(String email, String password) {
    //     String sql = "SELECT member_id, member_name, email, password FROM member WHERE email = :email AND password = :password";

    //     Map<String, Object> map = new HashMap<>();
    //     map.put("email", email);
    //     map.put("password", password);

    //     List<Member> member = namedParameterJdbcTemplate.query(sql, map, new MemberRowMapper());

    //     if (member.size() > 0) {
    //         return member.get(0);
    //     }

    //     return null;
    // }

    // Security 註冊會員
    @Override
    public Integer register(Member member) {
        String sql = "INSERT INTO member (member_name, email, password) VALUES (:memberName, :email, :password)";

        Map<String, Object> map = new HashMap<>();
        map.put("memberName", member.getMemberName());
        map.put("email", member.getEmail());
        map.put("password", member.getPassword());

        KeyHolder keyHolder = new GeneratedKeyHolder();

        namedParameterJdbcTemplate.update(sql, new MapSqlParameterSource(map), keyHolder);

        Integer memberId = keyHolder.getKey().intValue();

        return memberId;
    }

    // Security 透過 Email 取得會員LIST資料
    @Override
    public Member getMemberByEmail(String email) {
        String sql = "SELECT member_id, member_name, email, password FROM member WHERE email = :email";

        Map<String, Object> map = new HashMap<>();
        map.put("email", email);

        List<Member> memberList = namedParameterJdbcTemplate.query(sql, map, new MemberRowMapper());

        if (memberList.size() > 0) {
            return memberList.get(0);
        }

        else {
            return null;
        }
    }

    // Security 透過 Email 取得會員ID
    @Override
    public Integer getMemberIdByEmail(String email) {
        String sql = "SELECT member_id FROM member WHERE email = :email";

        Map<String, Object> map = new HashMap<>();
        map.put("email", email);

        Integer memberId = namedParameterJdbcTemplate.queryForObject(sql, map, Integer.class);

        return memberId;
    }

    // Security 透過 ID 取得會員資料
    @Override
    public Member getMemberById(Integer memberId) {
        String sql = "SELECT member_id, member_name, email, password FROM member WHERE member_id = :member_id";

        Map<String, Object> map = new HashMap<>();
        map.put("member_id", memberId);

        List<Member> memberList = namedParameterJdbcTemplate.query(sql, map, new MemberRowMapper());

        if (memberList.size() > 0) {
            return memberList.get(0);
        }

        else {
            return null;
        }
    }

    // Security 透過 ID 取得會員的角色
    @Override
    public List<Role> getRolesByMemberId(Integer memberId) {
        String sql = "SELECT role.role_id, role_name FROM role " +
                    "JOIN member_has_role ON role.role_id = member_has_role.role_id " +
                    "WHERE member_has_role.member_id = :member_id";

        Map<String, Object> map = new HashMap<>();
        map.put("member_id", memberId);

        List<Role> roleList = namedParameterJdbcTemplate.query(sql, map, new RoleRowMapper());

        return roleList;
    }

    // 更新會員 Email
    @Override
    public void updateEmail(Integer memberId, String newEmail) {
        String sql = "UPDATE member SET email = :email WHERE member_id = :member_id";

        Map<String, Object> map = new HashMap<>();
        map.put("email", newEmail);
        map.put("member_id", memberId);

        namedParameterJdbcTemplate.update(sql, map);

        return;
    }
}
