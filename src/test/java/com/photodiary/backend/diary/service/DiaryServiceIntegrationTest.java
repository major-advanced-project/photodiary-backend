package com.photodiary.backend.diary.service;

import com.photodiary.backend.diary.dto.DiaryTitleAndContent;
import com.photodiary.backend.diary.dto.ImageDiaryItem;
import com.photodiary.backend.diary.util.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.mock.web.MockMultipartFile;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@SpringBootTest
@Import(DiaryServiceIntegrationTest.MockConfig.class)
public class DiaryServiceIntegrationTest {

    @Autowired
    private DiaryService diaryService;

    @Autowired
    private KakaoMapApi kakaoMapApi;

    @Autowired
    private ChatgptApi chatgptApi;

    @TestConfiguration
    static class MockConfig {
        @Bean
        public Blip2ModelApi blip2ModelApi() {
            Blip2ModelApi mock = mock(Blip2ModelApi.class);
            when(mock.retreiveImageDescirptions(anyList())).thenAnswer(invocation -> {
                long blipStart = System.currentTimeMillis();

                Thread.sleep(1000); // BLIP 응답 지연 시뮬레이션
                ImageDiaryItem item = new ImageDiaryItem();
                item.setDescription("지연된 설명");

                long blipEnd = System.currentTimeMillis();
                System.out.println("🟦 BLIP 처리 시간: " + (blipEnd - blipStart) + "ms");

                return List.of(item);
            });
            return mock;
        }

        @Bean
        public MetadataExtractor metadataExtractor() {
            MetadataExtractor mock = mock(MetadataExtractor.class);
            doNothing().when(mock).extractMetadata(any());
            when(mock.getDateTime()).thenReturn(LocalDateTime.of(2023, 1, 1, 10, 30));

            GpsCoordinate gps = new GpsCoordinate();
            gps.latitude = 37.4979;
            gps.longitude = 127.0276;
            when(mock.getGpsCoordinate()).thenReturn(gps);

            return mock;
        }
    }

    @Test
    void testGenerateDiary_withFakeGpsAndRealKakao() {
        // given
        MockMultipartFile file = new MockMultipartFile(
                "test-image", "image.jpg", "image/jpeg", "dummy-content".getBytes()
        );

        // 🟩 KakaoMap API 단독 호출 시간 측정
        GpsCoordinate gps = new GpsCoordinate();
        gps.latitude = 37.4979;
        gps.longitude = 127.0276;

        long kakaoStart = System.currentTimeMillis();
        String place = kakaoMapApi.retrievePlaceName(gps);
        long kakaoEnd = System.currentTimeMillis();
        System.out.println("🟩 KakaoMap 단독 호출 시간: " + (kakaoEnd - kakaoStart) + "ms");
        System.out.println("🟩 반환된 장소명: " + place);

        // 🟥 GPT API 단독 호출 시간 측정
        ImageDiaryItem mockItem = new ImageDiaryItem();
        mockItem.setDescription("지연된 설명");
        mockItem.setDatetime(LocalDateTime.of(2023, 1, 1, 10, 30));
        mockItem.setLocation(place);
        String prompt = ChatgptPromptBuilder.buildDiaryPrompt(List.of(mockItem));

        long gptStart = System.currentTimeMillis();
        DiaryTitleAndContent gptResult = chatgptApi.retrieveDiary(prompt);
        long gptEnd = System.currentTimeMillis();
        System.out.println("🟥 GPT 단독 처리 시간: " + (gptEnd - gptStart) + "ms");

        // ✅ 전체 수행 시간 측정
        long totalStart = System.currentTimeMillis();
        DiaryTitleAndContent result = diaryService.generateDiary(List.of(file));
        long totalEnd = System.currentTimeMillis();
        System.out.println("✅ 전체 수행 시간: " + (totalEnd - totalStart) + "ms");

        // then
        assertThat(result).isNotNull();
        assertThat(result.getTitle()).isNotBlank();
        assertThat(result.getContent()).isNotBlank();
    }

}
