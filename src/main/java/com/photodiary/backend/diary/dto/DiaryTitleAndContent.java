package com.photodiary.backend.diary.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;


@Getter
@Builder
@AllArgsConstructor
@ToString
public class DiaryTitleAndContent {
    private String title;
    private String content;
}
