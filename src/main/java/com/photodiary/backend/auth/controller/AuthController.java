package com.photodiary.backend.auth.controller;

import com.photodiary.backend.auth.dto.LoginRequestDto;
import com.photodiary.backend.auth.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

@Slf4j
@RequestMapping("auth")
@RequiredArgsConstructor
@RestController
public class AuthController {
    private final AuthService authService;
    @PostMapping("/login")
    public ResponseEntity<Object> login(@RequestBody LoginRequestDto request){
        log.info("[login]");
        try{
            return ResponseEntity.ok(authService.login(request));
        }
        catch(RuntimeException e){
            log.error("error message = {}", e.getMessage());
            HashMap<Object, Object> body = new HashMap<>();
            body.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(body);
        }
    }
}
