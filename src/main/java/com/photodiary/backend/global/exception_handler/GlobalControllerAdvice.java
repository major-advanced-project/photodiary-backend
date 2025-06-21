package com.photodiary.backend.global.exception_handler;

import com.photodiary.backend.global.exception.JwtAuthenticationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@ControllerAdvice
public class GlobalControllerAdvice {
    @ExceptionHandler(JwtAuthenticationException.class)
    public ResponseEntity<Map<String, String>> handle_JwtAuthenticationException(JwtAuthenticationException e){
        log.info("[handle_JwtAuthenticationException]");
        return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
    }
}
