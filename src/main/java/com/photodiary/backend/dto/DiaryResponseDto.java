package com.photodiary.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
@Builder
public class DiaryResponseDto {
    private boolean success;
    private String message;
    private int imageCount;
    private String privacy;
    private List content;
}
