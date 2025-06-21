package com.photodiary.backend.global.exception;

public class EmptyDiaryList extends RuntimeException {
    public EmptyDiaryList(String message) {
        super(message);
    }
}
