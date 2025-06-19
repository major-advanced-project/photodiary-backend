package com.photodiary.backend.friend.dto;


import com.photodiary.backend.user.model.User;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class FindFriendResponseDto {
    private Long id;
    private String name;
    private String email;

    public static FindFriendResponseDto from(User targetUser) {
        return new FindFriendResponseDto(
                targetUser.getId(),
                targetUser.getUsername(),
                targetUser.getEmail()
        );
    }

}
