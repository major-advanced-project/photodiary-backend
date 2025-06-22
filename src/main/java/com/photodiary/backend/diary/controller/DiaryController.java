package com.photodiary.backend.diary.controller;

import com.photodiary.backend.diary.dto.SaveDiaryRequestDto;
import com.photodiary.backend.diary.dto.DiaryTitleAndContent;
import com.photodiary.backend.diary.dto.UpdateDiaryRequestDto;
import com.photodiary.backend.diary.service.DiaryService;
import com.photodiary.backend.diary.service.SaveDiaryService;
import com.photodiary.backend.diary.service.UpdateDiaryService;
import com.photodiary.backend.global.exception.CustomException;
import com.photodiary.backend.global.jwt.annotation.LoginUserId;
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
@RequestMapping("/diarys")
@RestController
public class DiaryController {
    private final DiaryService diaryService;
    private final UpdateDiaryService updateDiaryService;
    private final SaveDiaryService saveDiaryService;

    @PostMapping("/generate")
    public ResponseEntity<DiaryTitleAndContent> generateDiary(@RequestParam("images") List<MultipartFile> files) {
        if (files == null || files.isEmpty()) {
            log.info("[generate] images is null or empty");
            return ResponseEntity.badRequest().body(null);
        }

        DiaryTitleAndContent response = diaryService.generateDiary(files);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{diaryId}")
    public ResponseEntity<Object> updateDiary(@PathVariable long diaryId, @RequestBody UpdateDiaryRequestDto request, @LoginUserId Long userId){
        log.info("[updateDiary] diaryId = {} userId = {}", diaryId, userId);
        try{
            updateDiaryService.updateDiary(userId, diaryId, request);
            return ResponseEntity.ok(Map.of("message", "success"));
        }
        catch (CustomException e){
            log.error("[updateDiary] message = {}", e.getMessage());
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        }
    }

    @PostMapping
    public ResponseEntity<Object> saveDiary(@RequestPart List<MultipartFile> images, @RequestPart SaveDiaryRequestDto request,@LoginUserId Long userId){
        saveDiaryService.save(userId, images, request);
        return ResponseEntity.ok(Map.of("message", "success"));
    }

    @PostMapping("/generate2")
    public ResponseEntity<Map<String, Object>> mockGenerateDiary() {
        return ResponseEntity.ok(Map.of(
                "success", true,
                "title", "하루의 시작",
                "content", "오늘은 고양이와 산책하며 하루를 시작했다."
        ));
    }
}
