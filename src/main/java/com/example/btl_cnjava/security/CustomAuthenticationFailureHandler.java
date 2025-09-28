package com.example.btl_cnjava.security;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CustomAuthenticationFailureHandler implements AuthenticationFailureHandler {
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
                                        AuthenticationException exception) throws IOException, ServletException {
        // Ghi log lỗi ra console
        System.out.println("Lỗi đăng nhập: " + exception.getMessage());

        // Redirect kèm theo thông báo lỗi
        response.sendRedirect("/login?error=" + exception.getMessage());
    }
}
