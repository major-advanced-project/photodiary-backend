package com.photodiary.backend.global.interceptor;

import com.photodiary.backend.global.exception.JwtAuthenticationException;
import com.photodiary.backend.global.jwt.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.io.IOException;
import java.io.PrintWriter;

@Component
public class AuthInterceptor implements HandlerInterceptor {
    private final JwtUtil jwtUtil;

    public AuthInterceptor(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            response.setHeader("Access-Control-Allow-Origin", "https://ku-photodiary.vercel.app");
            response.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, PATCH, OPTIONS");
            response.setHeader("Access-Control-Allow-Headers", "*");
            response.setStatus(HttpServletResponse.SC_OK);
            return false; // OPTIONS 요청은 여기서 끝냄
        }

        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            writeErrorResponse(response, "토큰이 존재하지 않습니다", HttpServletResponse.SC_UNAUTHORIZED);
            return false;
        }

        String token = authHeader.substring(7);
        if(!jwtUtil.validateToken(token)){
            writeErrorResponse(response, "올바른 토큰이 아닙니다", HttpServletResponse.SC_UNAUTHORIZED);
            return false;
        }

        request.setAttribute("access_token", token);
        return true;
    }

    // JSON 에러 메시지 응답 메서드
    private void writeErrorResponse(HttpServletResponse response, String message, int statusCode) throws IOException {
        response.setStatus(statusCode);
        response.setContentType("application/json;charset=UTF-8");

        String json = String.format("{\"message\": \"%s\"}", message);

        try (PrintWriter writer = response.getWriter()) {
            writer.write(json);
            writer.flush();
        }
    }
}
