package com.photodiary.backend.friend.dto;


import com.photodiary.backend.user.model.User;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class AddFriendResponseDto {
    private Long userId;
    private Long friendId;

    public static AddFriendResponseDto from(Long userId, Long friendId) {
        return new AddFriendResponseDto(userId, friendId);
    }
}
