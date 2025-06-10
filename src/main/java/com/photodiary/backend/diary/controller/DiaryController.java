package com.photodiary.backend.diary.controller;

import com.photodiary.backend.diary.service.DiaryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@CrossOrigin(origins = "http://localhost:3000")
@Slf4j
@RequiredArgsConstructor
@RequestMapping("diary")
@RestController
public class DiaryController {
    private final DiaryService diaryService;

    @PostMapping("/generate")
    public ResponseEntity<Object> createDairy(
            @RequestPart("files") List<MultipartFile> files,
            @RequestParam("isPublic") boolean isPublic) {
        if (files == null) {
            log.info("[createDiary] files is null");
            return ResponseEntity.badRequest().build();
        }
        diaryService.createDiary(files, isPublic);
        return ResponseEntity.ok().build();
    }

}
