package com.photodiary.backend.diary.exception;

import com.photodiary.backend.global.exception.CustomException;

public class UserDiaryNotFoundException extends CustomException {
    public UserDiaryNotFoundException(String message) {
        super(message);
    }
}
