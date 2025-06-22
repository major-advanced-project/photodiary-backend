package com.photodiary.backend.friend.Exception;

import com.photodiary.backend.global.exception.CustomException;

public class NoFriendRequestException extends CustomException {
    public NoFriendRequestException(String message) {
        super(message);
    }
}
