package com.photodiary.backend.diary.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@Getter
@NoArgsConstructor
@ToString
public class ImageWithDescription {
    @JsonProperty(value = "filename")
    private String filename;

    @JsonProperty(value = "caption")
    private String description;
}
