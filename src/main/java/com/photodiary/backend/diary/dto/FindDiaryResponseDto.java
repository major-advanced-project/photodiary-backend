package com.photodiary.backend.diary.dto;

import com.photodiary.backend.diary.model.Diary;
import com.photodiary.backend.diary.model.Image;
import lombok.Builder;

import java.util.List;
import java.util.stream.Collectors;

@Builder
public record FindDiaryResponseDto(
        Long diaryId,
        String title,
        String content,
        List<ImageResponseDto> images,
        boolean isPublic

) {
    public static FindDiaryResponseDto entityToDto(Diary diary){
        return FindDiaryResponseDto.builder()
                .diaryId(diary.getId())
                .title(diary.getTitle())
                .content(diary.getContent())
                .images(diary.getImages().stream().map(ImageResponseDto::entityToDto).collect(Collectors.toList()))
                .isPublic(diary.isPublic())
                .build();
    }
}
