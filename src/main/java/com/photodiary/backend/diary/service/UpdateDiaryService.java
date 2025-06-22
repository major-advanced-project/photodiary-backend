package com.photodiary.backend.diary.service;

import com.photodiary.backend.diary.dto.UpdateDiaryRequestDto;
import com.photodiary.backend.diary.exception.UserDiaryNotFoundException;
import com.photodiary.backend.diary.model.Diary;
import com.photodiary.backend.diary.repository.DiaryRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class UpdateDiaryService {

    private final DiaryRepository diaryRepository;

    public void updateDiary(long userId, long diaryId, UpdateDiaryRequestDto request) {
        Diary diary = diaryRepository.findById(diaryId).orElseThrow(() -> new UserDiaryNotFoundException("존재하지 않는 일기입니다"));

        if(!diary.isWriter(userId)){
            throw new UserDiaryNotFoundException("존재하지 않는 일기입니다");
        }

        diary.update(request.title(), request.content(), request.isPublic());
    }
}
