package com.photodiary.backend.auth.service;

import com.photodiary.backend.auth.dto.LoginRequestDto;
import com.photodiary.backend.auth.dto.LoginResponseDto;
import com.photodiary.backend.global.jwt.JwtUtil;
import com.photodiary.backend.user.model.User;
import com.photodiary.backend.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class AuthService {
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    public LoginResponseDto login(LoginRequestDto request){
        Optional<User> optUser = userRepository.findByEmail(request.email());
        if(optUser.isPresent()) {
            User foundUser = optUser.get();
            if (request.password().equals(foundUser.getPassword())) {
                return new LoginResponseDto(true, jwtUtil.generateAccessToken());
            }
        }                  
        return new LoginResponseDto(false, null);
    }
}
