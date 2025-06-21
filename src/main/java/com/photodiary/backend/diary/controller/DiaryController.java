package com.photodiary.backend.diary.controller;

import com.photodiary.backend.diary.dto.DiaryTitleAndContent;
import com.photodiary.backend.diary.dto.UpdateDiaryRequestDto;
import com.photodiary.backend.diary.service.DiaryService;
import com.photodiary.backend.diary.service.UpdateDiaryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "http://localhost:3000")
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/diary")
@RestController
public class DiaryController {
    private final DiaryService diaryService;
    private final UpdateDiaryService updateDiaryService;

    @PostMapping("/generate")
    public ResponseEntity<DiaryTitleAndContent> generateDiary(List<MultipartFile> files){
        if(files == null){
            log.info("[generate] files is null");
            return ResponseEntity.badRequest().build();
        }
        DiaryTitleAndContent response = diaryService.generateDiary(files);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{diaryId}")
    public ResponseEntity<Object> updateDiary(@RequestParam long diaryId, @RequestBody UpdateDiaryRequestDto request){
        long userId = 1L;
        log.info("[updateDiary] diaryId = {} userId = {}", diaryId, userId);
        updateDiaryService.updateDiary(userId, diaryId, request);
        return ResponseEntity.ok(Map.of("message", "success"));
    }
}
