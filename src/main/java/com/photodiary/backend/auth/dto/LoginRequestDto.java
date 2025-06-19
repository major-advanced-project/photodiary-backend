package com.photodiary.backend.auth.dto;

public record LoginRequestDto(
        String email,
        String password
) {
}
