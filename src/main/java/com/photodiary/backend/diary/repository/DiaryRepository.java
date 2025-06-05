package com.photodiary.backend.diary.repository;

import com.photodiary.backend.diary.model.Diary;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DiaryRepository extends JpaRepository<Diary, Long> {
     Optional<Diary> findByUserIdAndId(long userId, long diaryId);
}
