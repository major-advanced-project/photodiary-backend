package com.photodiary.backend.friend.Exception;

import com.photodiary.backend.global.exception.CustomException;

public class UnauthorizedFriendActionException extends CustomException {
    public UnauthorizedFriendActionException(String message) {
        super(message);
    }
}
