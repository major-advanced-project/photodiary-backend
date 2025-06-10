package com.photodiary.backend.diary.controller;

import com.photodiary.backend.diary.dto.DiaryTitleAndContent;
import com.photodiary.backend.diary.service.DiaryService;
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

    @PostMapping("/generate")
    public ResponseEntity<DiaryTitleAndContent> generateDiary(List<MultipartFile> files){
        if(files == null){
            log.info("[generate] files is null");
            return ResponseEntity.badRequest().build();
        }
        DiaryTitleAndContent response = diaryService.generateDiary(files);
        return ResponseEntity.ok(response);
    }

//    @PostMapping("/generate2")
//    public ResponseEntity<Map<String, Object>> mockGenerateDiary() {
//        return ResponseEntity.ok(Map.of(
//                "success", true,
//                "title", "하루의 시작",
//                "content", "오늘은 고양이와 산책하며 하루를 시작했다."
//        ));
//    }


}
