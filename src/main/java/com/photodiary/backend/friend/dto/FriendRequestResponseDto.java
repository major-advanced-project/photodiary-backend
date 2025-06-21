package com.photodiary.backend.friend.dto;

import com.photodiary.backend.friend.model.FriendStatus;
import lombok.Builder;

import java.time.LocalDate;

@Builder
public record FriendRequestResponseDto(
        Long id,
        String username,
        String email,
        LocalDate requestedAt,
        FriendStatus status
) {
}
