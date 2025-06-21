package com.photodiary.backend.diary.controller;

import com.photodiary.backend.diary.dto.FindDiaryResponseDto;
import com.photodiary.backend.diary.service.FindDiaryService;
import com.photodiary.backend.global.jwt.annotation.LoginUserId;
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
    public ResponseEntity<FindDiaryResponseDto> findDiary(@PathVariable("diaryId") Long diaryId,@LoginUserId Long userId){
        log.info("[findDiary] diaryId = {}", diaryId);
        return ResponseEntity.ok(findDiaryService.findUserDiary(userId, diaryId));
    }

    @GetMapping
    public ResponseEntity<List<FindDiaryResponseDto>> findDiaryList(@LoginUserId Long userId){
        log.info("[findDiarys]");
        return ResponseEntity.ok(findDiaryService.findUserDairyList(userId));
    }

}
