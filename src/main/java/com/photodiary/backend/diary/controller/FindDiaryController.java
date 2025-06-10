package com.photodiary.backend.diary.controller;

import com.photodiary.backend.diary.dto.FindDiaryResponseDto;
import com.photodiary.backend.diary.service.FindDiaryService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("diarys")
public class FindDiaryController {
    private final FindDiaryService findDiaryService;
    @GetMapping("/{diaryId}")
    public ResponseEntity<FindDiaryResponseDto> findDiary(@PathVariable("diaryId") Long diaryId){
        log.info("[findDiary] diaryId = {}", diaryId);
        long userId = 1L;
        return ResponseEntity.ok(findDiaryService.findUserDiary(userId, diaryId));
    }

    @GetMapping
    public ResponseEntity<List<FindDiaryResponseDto>> findDiaryList(){
        log.info("[findDiarys]");
        long userId = 1L;
        return ResponseEntity.ok(findDiaryService.findUserDairyList(userId));
    }

}
