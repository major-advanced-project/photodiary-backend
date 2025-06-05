package com.photodiary.backend.diary.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@ToString
@AllArgsConstructor
@Setter
public class ImageDiaryItem {
    @JsonProperty(value = "filename")
    private String filename;

    @JsonProperty(value = "caption")
    private String description;

    private LocalDateTime datetime;
    private String location;
}
