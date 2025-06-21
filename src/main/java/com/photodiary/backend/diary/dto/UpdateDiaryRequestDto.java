package com.photodiary.backend.diary.dto;

public record UpdateDiaryRequestDto(
        String title,
        String content
) {
}
