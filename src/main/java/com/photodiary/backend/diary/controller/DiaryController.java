package com.photodiary.backend.diary.controller;

import com.photodiary.backend.diary.service.DiaryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("diary")
@RestController
public class DiaryController {
    private final DiaryService diaryService;

    @PostMapping
    public ResponseEntity<Object> createDairy(List<MultipartFile> files, boolean isPublic){
        if(files == null){
            log.info("[createDiary] files is null");
            return ResponseEntity.badRequest().build();
        }
        diaryService.createDiary(files, isPublic);
        return ResponseEntity.ok().build();
    }
}
