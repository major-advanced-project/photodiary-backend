package com.photodiary.backend.friend.dto;

import lombok.Builder;

import java.time.LocalDate;

@Builder
public record FriendRequestResponseDto(
        String username,
        String email,
        LocalDate requestedAt
) {
}
