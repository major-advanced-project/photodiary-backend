package com.photodiary.backend.diary.dto;

import com.photodiary.backend.diary.model.Image;
import lombok.Builder;

@Builder
public record ImageResponseDto(
        Long imageId,
        String imageUrl
) {
    public static ImageResponseDto entityToDto(Image image){
        return ImageResponseDto.builder()
                .imageId(image.getId())
                .imageUrl(image.getImageUrl())
                .build();
    }
}
