package com.photodiary.backend.diary.service;

import com.photodiary.backend.diary.dto.ImageWithDescription;
import com.photodiary.backend.diary.util.*;
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

    public void createDiary(List<MultipartFile> files, boolean isPublic) {
        // 각 파일에 유일한 이름을 부여하기 위해서 임시파일 생성
        List<File> renamedImages = files.stream()
                .map(multipartFile -> convertToRenamedTempFile(multipartFile))
                .collect(Collectors.toList());

        // todo BLIP2 서버 호출 후 텍스트 변환 결과 받기
        List<ImageWithDescription> imageWithDescriptions = blip2ModelApi.retreiveImageDescirptions(renamedImages);

        for (MultipartFile multipartFile : files) {
            // todo 메타데이터로부터 시간정보와 장소정보 추출
            metadataExtractor.extractMetadata(multipartFile);
            LocalDateTime dateTime = metadataExtractor.getDateTime();
            GpsCoordinate gpsCoordinate = metadataExtractor.getGpsCoordinate();

            // todo 카카오맵으로부터 장소명 추출
            String placeName = kakaoMapApi.retrievePlaceName(gpsCoordinate);
            System.out.println("placeName = " + placeName);
        }

        // todo GPT에게 전달할 요청을 생성

        // todo GPT에게 일기 생성 요청
        chatgptApi.retrieveDiary();

        // 임시 파일 삭제
        renamedImages.stream().forEach(File::delete);

        // todo 클라이언트에게 일기 생성 결과 반환
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
}