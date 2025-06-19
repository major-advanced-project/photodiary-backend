package com.photodiary.backend.friend.Exception;

public class NoFriendFoundException extends RuntimeException {
    public NoFriendFoundException(String message) {
        super(message);
    }
}
