package com.photodiary.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class DiaryResponseDto {
    private boolean success;
    private String message;
    private int imageCount;
    private String privacy;
    private String content;
}
