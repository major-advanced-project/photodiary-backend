package com.photodiary.backend.diary.service;

import com.photodiary.backend.diary.dto.SaveDiaryRequestDto;
import com.photodiary.backend.diary.model.Diary;
import com.photodiary.backend.diary.model.Image;
import com.photodiary.backend.diary.repository.DiaryRepository;
import com.photodiary.backend.global.common.S3Uploader;
import com.photodiary.backend.global.exception.UserNotFoundException;
import com.photodiary.backend.user.model.User;
import com.photodiary.backend.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class SaveDiaryService {
    private final UserRepository userRepository;
    private final DiaryRepository diaryRepository;
    private final S3Uploader s3Uploader;

    public void save(long userId, List<MultipartFile> images, SaveDiaryRequestDto request) {
        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("존재하지 않는 유저입니다."));

        Diary newDiary = Diary.builder()
                .title(request.title())
                .content(request.content())
                .user(user)
                .isPublic(request.isPublic())
                .build();

        images.stream().forEach(image->{
            String s3Url = s3Uploader.upload(image);
            newDiary.addImage(Image.builder().imageUrl(s3Url).build());
        });

        diaryRepository.save(newDiary);
    }
}
