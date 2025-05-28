package com.photodiary.backend.controller;


import com.photodiary.backend.dto.DiaryResponseDto;
import com.photodiary.backend.service.ImageProcessService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/images")
public class ImageUploadController {

    private final ImageProcessService imageProcessService;

    @PostMapping("/upload")
    public ResponseEntity<DiaryResponseDto> uploadImages(
            @RequestParam("images") List<MultipartFile> images,
            @RequestParam("privacy") String privacy
    ) {
        try {
            Map<String, Object> result = imageProcessService.sendImagesToFastApi(images, privacy);

            DiaryResponseDto responseDto = DiaryResponseDto.builder()
                    .success((Boolean) result.getOrDefault("success", false))
                    .message((String) result.getOrDefault("message", ""))
                    .imageCount((Integer) result.getOrDefault("imageCount", 0))
                    .privacy((String) result.getOrDefault("privacy", "private"))
                    .content((List) result.getOrDefault("content", ""))
                    .build();

            return ResponseEntity.ok(responseDto);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body(
                    DiaryResponseDto.builder()
                            .success(false)
                            .message("이미지 처리 중 오류 발생")
                            .imageCount(0)
                            .privacy(privacy)
                            .content(null)
                            .build()
            );
        }
    }
}
