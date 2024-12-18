package com.example.bookstore.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
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

    @Autowired
    private MyAuthenticationEntryPoint myAuthenticationEntryPoint;


    // 設定密碼加密器的方法
    @Bean
    public PasswordEncoder passwordEncoder() {
        // 使用 BCryptPasswordEncoder 進行密碼加密
        return new BCryptPasswordEncoder();
    }

    // 設定安全過濾器鏈 (SecurityFilterChain) 的方法
    @SuppressWarnings("deprecation")
    @Bean
    public SecurityFilterChain SecurityFilterChain(HttpSecurity http) throws Exception {

        return http
        .csrf(csrf -> csrf.disable()) // 關閉 CSRF
        .authorizeRequests(request -> request
            .requestMatchers(HttpMethod.GET, "/book/*").authenticated()
            .requestMatchers(
                "/", "/index.html","/login.html", "/register.html", "/register", 
                "/profile","/order.html","/bookList.html").permitAll() // 這些請求不需要登入
            .anyRequest().authenticated() // 其他請求需要登入
        )
        .formLogin(form -> form
            .loginPage("/login") // 登入頁面
            // .loginProcessingUrl("/login.html") // 登入請求的 URL
            // .defaultSuccessUrl("/hello.html", true) // 登入成功後的 URL
            .successHandler(customAuthenticationSuccessHandler()) // 登入成功後的處理器
            .failureUrl("/login") // 登入失敗後的 URL
            .permitAll()
        )
        .logout(logout -> logout
            .logoutUrl("/logout") // 登出請求的 URL
            .logoutSuccessUrl("/login") // 登出成功後的 URL
            .invalidateHttpSession(true) // 登出後，使 HttpSession 失效
            .deleteCookies("JSESSIONID") // 登出後，刪除 JSESSIONID Cookie
        )
        .exceptionHandling(exceptionHandling -> exceptionHandling
            .authenticationEntryPoint(myAuthenticationEntryPoint) // 除理401自定義認證失敗後的處理器
        ) 
        .sessionManagement(sessionManagement -> sessionManagement
            .invalidSessionUrl("/login") // Session 失效後，導向 /login.html
            .maximumSessions(1)
            .expiredUrl("/login") // Session 過期後，導向 /login.html
        )
        .build();
        
    }

    // 自定義登入成功後的處理器
    @Bean
    public AuthenticationSuccessHandler customAuthenticationSuccessHandler() {
        return (request, response, authentication) -> {
            
            if (authentication != null) {
                String email = authentication.getName();
                Integer memberId = memberDao.getMemberIdByEmail(email);
                response.sendRedirect("/" + memberId + "/");// 登入成功後，導向 /{memberId}/
            }

            else {
                response.sendRedirect("/login");// 登入失敗後，導向 /login.html
            }
        };
    }
}
