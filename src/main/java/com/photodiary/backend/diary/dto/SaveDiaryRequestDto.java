package com.photodiary.backend.diary.dto;

public record SaveDiaryRequestDto(
       String title,
       String content,
       boolean isPublic
) {
}
