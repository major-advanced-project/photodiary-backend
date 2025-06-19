package com.photodiary.backend.auth.dto;

public record LoginResponseDto(
        boolean success,
        String accessToken
) {
}
