package com.photodiary.backend.diary.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.photodiary.backend.diary.dto.ImageDiaryItem;

import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

/**
 * 감정 중심의 하루 일기 생성을 위한 GPT 프롬프트 빌더
 */
public class ChatgptPromptBuilder {

    private static final String EMOTIONAL_DIARY_PROMPT_HEADER = String.join("\n\n",
            "다음은 한 사람이 하루 동안 찍은 사진들입니다.",
            "각 항목에는 사진이 찍힌 시간, 장소, 장면 묘사가 담겨 있습니다.",
            "",
            "이 정보를 참고하여, 그 사람이 하루를 회상하며 쓴 감정적인 일기를 작성해 주세요.",
            "",
            "일기는 다음 형식을 따라 주세요:",
            "- 제목: 하루의 감정을 잘 표현한 문장 한 줄",
            "- 내용: 사진을 시간 순으로 연결해 하나의 이야기처럼 서술",
            "",
            "💡 장소 이름과 촬영 시간을 자연스럽게 문장에 녹여 주세요.",
            "💡 장면마다 느낄 수 있는 감정을 중심으로 표현해 주세요.",
            "",
            "사진 목록:"

    );


    /**
     * 이미지 일기 항목들을 기반으로 GPT 프롬프트를 생성합니다.
     *
     * @param imageRecords 시간, 장소, 설명이 포함된 이미지 일기 항목 리스트
     * @return 완성된 GPT 프롬프트 문자열
     */
    public static String buildDiaryPrompt(List<ImageDiaryItem> imageRecords) {
        List<ImageDiaryItem> sorted = new ArrayList<>(imageRecords);
        sorted.sort(Comparator.comparing(ImageDiaryItem::getDatetime));

        List<String> photoLines = new ArrayList<>();

        for (ImageDiaryItem item : sorted) {
            String timeStr = item.getDatetime()
                    .atZone(ZoneId.of("Asia/Seoul"))
                    .format(DateTimeFormatter.ofPattern("yyyy년 M월 d일 a h시 m분", Locale.KOREAN));


            String place = item.getLocation() != null ? item.getLocation() : "이름 없는 장소";
            String desc = item.getDescription() != null ? item.getDescription() : "장면 설명 없음";

            // ✅ 콘솔 출력
            System.out.println("[🕒 시간] " + timeStr);
            System.out.println("[📍 장소] " + place);
            System.out.println("[🖼️ 설명] " + desc);
            System.out.println();

            photoLines.add(String.format("- [%s, %s] %s", timeStr, place, desc));
        }


        String joinedBody = String.join("\n", photoLines);

        return String.join("\n\n", EMOTIONAL_DIARY_PROMPT_HEADER, joinedBody);
    }

}
