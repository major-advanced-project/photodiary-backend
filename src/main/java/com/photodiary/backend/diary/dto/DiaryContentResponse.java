package com.photodiary.backend.diary.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;


@Getter
@Builder
@AllArgsConstructor
public class DiaryContentResponse {
    private String content;
}
