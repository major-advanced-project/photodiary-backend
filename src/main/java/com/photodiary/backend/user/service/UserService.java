package com.photodiary.backend.user.service;

import com.photodiary.backend.user.dto.SignupRequestDto;
import com.photodiary.backend.user.exception.AlreadySignedUpEmailException;
import com.photodiary.backend.user.model.User;
import com.photodiary.backend.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserService {
    private final UserRepository userRepository;
    public void signup(SignupRequestDto request) {
        log.info("[signup]");

        Optional<User> optUser = userRepository.findByEmail(request.email());
        if(optUser.isPresent()){
            throw new AlreadySignedUpEmailException("이미 회원가입에 사용된 이메일입니다.");
        }
        User newUser = User.builder()
                .email(request.email())
                .password(request.password())
                .username(request.username())
                .build();

        userRepository.save(newUser);
    }
}
