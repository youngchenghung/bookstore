package com.example.bookstore.security;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class MyAuthenticationEntryPoint implements AuthenticationEntryPoint {

    // 未驗證的請求處理 (回應 401)
    // 避免瀏覽器自動彈出帳密輸入框，通過設置 WWW-Authenticate 標頭來告訴瀏覽器這不是一個基本認證("Basic Authentication")請求
    // 而是自定義的認證方式("FormBased")-這樣可以防止瀏覽器自動彈出登入框。
    @Override
    public void commence(HttpServletRequest request, 
                        HttpServletResponse response, 
                        AuthenticationException authenticationException ) throws IOException {
        // 設定回應狀態碼
        response.setHeader("WWW-Authenicat", "FormBased");

        // 回應錯誤訊息
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
    }
}
