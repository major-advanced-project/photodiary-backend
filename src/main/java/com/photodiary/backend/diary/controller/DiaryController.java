package com.photodiary.backend.diary.controller;

import com.photodiary.backend.diary.dto.SaveDiaryRequestDto;
import com.photodiary.backend.diary.dto.DiaryTitleAndContent;
import com.photodiary.backend.diary.dto.UpdateDiaryRequestDto;
import com.photodiary.backend.diary.service.DiaryService;
import com.photodiary.backend.diary.service.SaveDiaryService;
import com.photodiary.backend.diary.service.UpdateDiaryService;
import com.photodiary.backend.global.exception.CustomException;
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
    public ResponseEntity<DiaryTitleAndContent> generateDiary(List<MultipartFile> files){
        if(files == null){
            log.info("[generate] files is null");
            return ResponseEntity.badRequest().build();
        }
        DiaryTitleAndContent response = diaryService.generateDiary(files);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{diaryId}")
    public ResponseEntity<Object> updateDiary(@PathVariable long diaryId, @RequestBody UpdateDiaryRequestDto request){
        long userId = 1L;
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
    public ResponseEntity<Object> saveDiary(@RequestPart List<MultipartFile> images, @RequestPart SaveDiaryRequestDto request){
        long userId = 1L;
        saveDiaryService.save(userId, images, request);
        return ResponseEntity.ok(Map.of("message", "success"));
    }
}
