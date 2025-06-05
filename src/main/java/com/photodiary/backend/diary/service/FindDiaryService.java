package com.photodiary.backend.diary.service;

import com.photodiary.backend.diary.dto.FindDiaryResponseDto;
import com.photodiary.backend.diary.model.Diary;
import com.photodiary.backend.diary.repository.DiaryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class FindDiaryService {
    private final DiaryRepository diaryRepository;

    public FindDiaryResponseDto findUserDiary(long userId, long diaryId) {
        Optional<Diary> opt = diaryRepository.findByUserIdAndId(userId, diaryId);
        if(opt.isEmpty()){
            throw new RuntimeException("일기가 존재하지 않습니다");
        }
        return FindDiaryResponseDto.entityToDto(opt.get());
    }

    public List<FindDiaryResponseDto> findUserDairyList(long userId) {
        List<Diary> diaryList = diaryRepository.findAllByUserId(userId);
        return diaryList.stream()
                .map(FindDiaryResponseDto::entityToDto)
                .collect(Collectors.toList());
    }
}
