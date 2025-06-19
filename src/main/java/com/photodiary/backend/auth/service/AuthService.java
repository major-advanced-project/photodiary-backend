package com.photodiary.backend.auth.service;

import com.photodiary.backend.auth.dto.LoginRequestDto;
import com.photodiary.backend.auth.dto.LoginResponseDto;
import com.photodiary.backend.global.exception.InvalidUserPasswordExecption;
import com.photodiary.backend.global.exception.UserNotFoundException;
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
    public LoginResponseDto login(LoginRequestDto request) {
        Optional<User> optUser = userRepository.findByEmail(request.email());
        if(optUser.isEmpty()) {
            throw new UserNotFoundException("존재하지 않는 유저입니다");
        }

        User foundUser = optUser.get();
        if (!request.password().equals(foundUser.getPassword())) {
            throw new InvalidUserPasswordExecption("비밀번호가 일치하지 않습니다");
        }

        return new LoginResponseDto(jwtUtil.generateAccessToken(foundUser.getId(), foundUser.getEmail()));
    }
}
