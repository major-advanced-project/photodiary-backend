package com.photodiary.backend.friend.Exception;

public class FriendAlreadyExistsException extends RuntimeException {
    public FriendAlreadyExistsException(String message) {
        super(message);
    }
}
