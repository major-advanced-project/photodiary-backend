package com.photodiary.backend.auth.controller;

import com.photodiary.backend.auth.dto.LoginRequestDto;
import com.photodiary.backend.auth.dto.LoginResponseDto;
import com.photodiary.backend.auth.service.AuthService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequestMapping("auth")
@RestController
public class AuthController {
    private final AuthService authService;
    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(@RequestBody LoginRequestDto request){
        log.info("[login]");
        return ResponseEntity.ok(authService.login(request));
    }
}
