package com.photodiary.backend.diary.service;

import com.photodiary.backend.diary.dto.DiaryTitleAndContent;
import com.photodiary.backend.diary.dto.ImageDiaryItem;
import com.photodiary.backend.diary.util.*;
import com.photodiary.backend.global.common.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class DiaryService {
    private final MetadataExtractor metadataExtractor;
    private final KakaoMapApi kakaoMapApi;
    private final Blip2ModelApi blip2ModelApi;
    private final ChatgptApi chatgptApi;
    private final S3Uploader s3Uploader;

    public DiaryTitleAndContent generateDiary(List<MultipartFile> files) {
        // 각 파일에 유일한 이름을 부여하기 위해서 임시파일 생성
        List<File> renamedImages = files.stream()
                .map(multipartFile -> convertToRenamedTempFile(multipartFile))
                .collect(Collectors.toList());

        // todo BLIP2 서버 호출 후 텍스트 변환 결과 받기
        List<ImageDiaryItem> imageDiaryItems = blip2ModelApi.retreiveImageDescirptions(renamedImages);

        for (int i = 0; i < files.size(); i++) {
            MultipartFile multipartFile = files.get(i);

            // 1. 메타데이터 추출을 위한 임시 파일 생성
            File tempFile = convertToRenamedTempFile(multipartFile);  // 이미 만든 함수 활용

            // 2. 메타데이터 추출
            metadataExtractor.extractMetadata(tempFile);
            LocalDateTime dateTime = metadataExtractor.getDateTime();
            GpsCoordinate gpsCoordinate = metadataExtractor.getGpsCoordinate();

            // 3. 장소명 추출
            String placeName = kakaoMapApi.retrievePlaceName(gpsCoordinate);
            System.out.println("placeName = " + placeName);

            // 4. 정보 주입
            ImageDiaryItem item = imageDiaryItems.get(i);
            item.setDatetime(dateTime);
            item.setLocation(placeName);

            // 5. 이미지 URL 저장 (S3 업로드는 여전히 MultipartFile로 진행)
            String imageUrl = s3Uploader.upload(multipartFile);
            System.out.println("imageUrl = " + imageUrl);

            // 6. 메타데이터용 임시 파일 삭제
            tempFile.delete();
        }


        // todo GPT에게 전달할 요청을 생성

        String prompt = ChatgptPromptBuilder.buildDiaryPrompt(imageDiaryItems);

        // todo GPT에게 일기 생성 요청
        DiaryTitleAndContent diaryTitleAndContent = chatgptApi.retrieveDiary(prompt);

        // 임시 파일 삭제
        renamedImages.stream().forEach(File::delete);

        // todo 클라이언트에게 일기 생성 결과 반환
        return diaryTitleAndContent;

    }

    private File convertToRenamedTempFile(MultipartFile multipartFile){
        try{
            File convFile = File.createTempFile(String.valueOf(UUID.randomUUID()), multipartFile.getOriginalFilename());
            try (FileOutputStream fos = new FileOutputStream(convFile)) {
                fos.write(multipartFile.getBytes());
            }
            return convFile;
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private File convertToFile(MultipartFile multipartFile) {
        try {
            String originalFilename = multipartFile.getOriginalFilename();
            File convFile = File.createTempFile("temp_", originalFilename);
            multipartFile.transferTo(convFile);
            return convFile;
        } catch (IOException e) {
            throw new RuntimeException("MultipartFile -> File 변환 실패", e);
        }
    }
}
