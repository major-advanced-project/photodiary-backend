package com.photodiary.backend.diary.service;

import com.photodiary.backend.diary.dto.DiaryTitleAndContent;
import com.photodiary.backend.diary.dto.ImageDiaryItem;
import com.photodiary.backend.diary.util.ChatgptApi;
import com.photodiary.backend.diary.util.ChatgptPromptBuilder;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.List;

@SpringBootTest
public class DiaryServiceTest {

    @Autowired
    private ChatgptApi chatgptApi;

    @Test
    void testGptDiaryGeneration_withDummyData() {
        // 1. 더미 데이터 구성
        List<ImageDiaryItem> dummyImages = List.of(
                new ImageDiaryItem("img1.jpg", "아침 햇살이 비치는 골목길에서 고양이가 길게 기지개를 켜고 있다.",
                        LocalDateTime.parse("2025-05-29T08:15:00"), "서울특별시 광진구 자양동"),
                new ImageDiaryItem("img2.jpg", "단풍이 짙게 물든 공원에서 자전거를 타는 아이.",
                        LocalDateTime.parse("2025-05-29T09:00:00"), "서울특별시 광진구 자양동"),
                new ImageDiaryItem("img4.jpg", "강가 산책로를 따라 조깅하는 사람들, 바람에 흔들리는 나뭇잎.",
                        LocalDateTime.parse("2025-05-29T10:20:00"), "서울특별시 광진구 자양나루공원"),
                new ImageDiaryItem("img3.jpg", "조용한 카페 테라스에서 책을 읽으며 커피를 마시는 사람.",
                        LocalDateTime.parse("2025-05-29T09:45:00"), "서울특별시 광진구 자양동 카페거리"),
                new ImageDiaryItem("img5.jpg", "맑은 하늘과 구름이 한강 수면에 아름답게 반사되고 있다.",
                        LocalDateTime.parse("2025-05-29T11:00:00"), "서울특별시 광진구 한강변")
        );

        // 2. 프롬프트 생성
        String prompt = ChatgptPromptBuilder.buildDiaryPrompt(dummyImages);
        System.out.println("==== GENERATED PROMPT ====\n" + prompt);

        // 3. GPT API 호출
        DiaryTitleAndContent diary = chatgptApi.retrieveDiary(prompt);
        System.out.println("==== GPT RESPONSE ====\n" + diary);
    }
}