package com.photodiary.backend.diary.repository;

import com.photodiary.backend.diary.model.Diary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DiaryRepository extends JpaRepository<Diary, Long> {
    Optional<Diary> findByUserIdAndId(long userId, long diaryId);

    List<Diary> findAllByUserId(long userId);
}
