package com.photodiary.backend.global.interceptor;

import com.photodiary.backend.global.exception.JwtAuthenticationException;
import com.photodiary.backend.global.jwt.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class AuthInterceptor implements HandlerInterceptor {
    private final JwtUtil jwtUtil;

    public AuthInterceptor(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new JwtAuthenticationException("토큰이 존재하지 않습니다");
        }

        String token = authHeader.substring(7);
        if(!jwtUtil.validateToken(token)){
            throw new JwtAuthenticationException("올바른 토큰이 아닙니다");
        }

        request.setAttribute("access_token", token);
        return true;
    }
}
