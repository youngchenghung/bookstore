package com.example.bookstore.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.Customizer;

import com.example.bookstore.dao.MemberDao;



@Configuration
@EnableWebSecurity
public class MySecurityConfig {

    @Autowired
    private MemberDao memberDao;

    // 設定密碼加密器的方法
    @Bean
    public PasswordEncoder passwordEncoder() {
        // 使用 BCryptPasswordEncoder 進行密碼加密
        return new BCryptPasswordEncoder();
    }

    // 設定安全過濾器鏈 (SecurityFilterChain) 的方法
    @Bean
    public SecurityFilterChain SecurityFilterChain(HttpSecurity http) throws Exception {

        return http
        .csrf(csrf -> csrf.disable()) // 關閉 CSRF
        .authorizeRequests(request -> request
            .requestMatchers("/member", "/member.html", "/register", "/booklist", "/booklist.html", "/books", "/profile.html").permitAll() // 放行 /member 目錄下所有請求
            .anyRequest().authenticated() // 其他請求需要登入
        )
        .formLogin(form -> form
            .loginPage("/login.html") // 登入頁面
            .loginProcessingUrl("/login.html") // 登入請求的 URL
            // .defaultSuccessUrl("/hello.html", true) // 登入成功後的 URL
            .successHandler(customAuthenticationSuccessHandler()) // 登入成功後的處理器
            .failureUrl("/login.html?error") // 登入失敗後的 URL
            .permitAll()
        )
        .logout(logout -> logout
            .logoutUrl("/logout") // 登出請求的 URL
            .logoutSuccessUrl("/login.html") // 登出成功後的 URL
            .invalidateHttpSession(true) // 登出後，使 HttpSession 失效
            .deleteCookies("JSESSIONID") // 登出後，刪除 JSESSIONID Cookie
        )
        .httpBasic(Customizer.withDefaults())
        .build();
    }

    // 自定義登入成功後的處理器
    @Bean
    public AuthenticationSuccessHandler customAuthenticationSuccessHandler() {
        return (request, response, authentication) -> {
            
            if (authentication != null) {
                String email = authentication.getName();
                Integer memberId = memberDao.getMemberIdByEmail(email);
                response.sendRedirect("/" + memberId + "/profile.html");// 登入成功後，導向 /{memberId}/profile.html
            }

            else {
                response.sendRedirect("/login.html?error");// 登入失敗後，導向 /login.html?error
            }
        };
    }
}
