package com.photodiary.backend.friend.dto;

public record FriendRequestActionDto(
        FriendAction action
) {
    public enum FriendAction {
        ACCEPT, DECLINE
    }
}
