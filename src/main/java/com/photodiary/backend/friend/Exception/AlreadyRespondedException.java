package com.photodiary.backend.friend.Exception;

import com.photodiary.backend.global.exception.CustomException;

public class AlreadyRespondedException extends CustomException {
    public AlreadyRespondedException(String message) {
        super(message);
    }
}
