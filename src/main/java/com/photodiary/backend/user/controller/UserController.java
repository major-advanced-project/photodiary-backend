package com.photodiary.backend.user.controller;

import com.photodiary.backend.user.dto.SignupRequestDto;
import com.photodiary.backend.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

@Slf4j
@RequestMapping("users")
@RequiredArgsConstructor
@RestController
public class UserController {
    private final UserService userService;
    @PostMapping("/signup")
    public ResponseEntity<Object> signup(@RequestBody SignupRequestDto request){
        log.info("[signup]");
        try{
            userService.signup(request);
            HashMap<Object, Object> body = new HashMap<>();
            body.put("message", "success");
            return ResponseEntity.ok(body);
        }
        catch(RuntimeException e){
            log.error("error message = {}", e.getMessage());
            HashMap<Object, Object> body = new HashMap<>();
            body.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(body);
        }
    }
}
