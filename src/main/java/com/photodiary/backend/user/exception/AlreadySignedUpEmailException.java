package com.photodiary.backend.user.exception;

public class AlreadySignedUpEmailException extends RuntimeException {
    public AlreadySignedUpEmailException(String message) {
        super(message);
    }
}
