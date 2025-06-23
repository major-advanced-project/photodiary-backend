package com.photodiary.backend.friend.dto;

import lombok.Builder;


@Builder
public record FindFriendByIdResponse(String username) {
}
