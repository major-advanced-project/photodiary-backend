package com.photodiary.backend.diary.exception;

import com.photodiary.backend.global.exception.CustomException;

public class MetadataNotFoundExcpetion extends CustomException {
    public MetadataNotFoundExcpetion(String message) {
        super(message);
    }
}
