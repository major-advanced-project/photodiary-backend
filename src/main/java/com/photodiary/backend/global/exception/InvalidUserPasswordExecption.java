package com.photodiary.backend.global.exception;

public class InvalidUserPasswordExecption extends RuntimeException {
    public InvalidUserPasswordExecption(String message) {
        super(message);
    }
}
