package com.photodiary.backend.user.dto;

public record SignupRequestDto(
        String username,
        String email,
        String password
) {

}
