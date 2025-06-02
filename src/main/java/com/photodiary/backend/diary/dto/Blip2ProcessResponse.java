package com.photodiary.backend.diary.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class Blip2ProcessResponse {
    List<ImageWithDescription> results;
}
