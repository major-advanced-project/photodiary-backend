package com.photodiary.backend.diary.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.photodiary.backend.diary.dto.ImageDiaryItem;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * 감정 중심의 하루 일기 생성을 위한 GPT 프롬프트 빌더
 */
public class ChatgptPromptBuilder {

    private static final String EMOTIONAL_DIARY_PROMPT_HEADER = String.join("\n\n",
            "다음은 어떤 하루를 기록한 사진들입니다. 각 항목에는 촬영 시간, 장소, 그리고 사진에 대한 묘사가 담겨 있습니다.",
            "이 정보를 바탕으로, 한 사람이 하루를 되돌아보며 작성한 감정 중심의 일기를 만들어 주세요.",
            "📝 일기에는 제목과 본문이 모두 포함되어야 합니다.",
            "📌 다음 형식을 반드시 따라 주세요:",
            "- 첫 줄: 제목: [제목 텍스트]",
            "- 두 번째 줄부터: 내용: [하루를 회상하는 일기 본문]",
            "🎯 아래 조건들을 지켜 주세요:",
            "1. **시간의 흐름에 따라** 이야기 구조가 진행되어야 합니다. 아침 → 낮 → 저녁처럼 자연스럽게 전개해 주세요.",
            "2. 각 사진의 **장소 이름은 반드시 글에 포함**시켜 주세요. 단, 너무 기계적으로 나열하지 말고, 자연스럽게 섞어주세요.",
            "3. 각 장면에서 느낄 수 있는 **감정(설렘, 평온함, 고요함, 외로움 등)을 중심으로** 서술해 주세요.",
            "4. **사람다운 어조**로, 일상적인 말투로 작성해 주세요. 너무 설명식이거나 딱딱한 문체는 지양해 주세요.",
            "5. **시간 정보는 서술에 녹여서** 반영해 주세요. 예: \"오후 2시쯤, 잠실동의 거리엔 햇살이 부드럽게 내려앉아 있었다.\"",
            "6. 모든 내용을 하나의 일기로 연결해주세요. 사진들을 별개의 에피소드로 나열하지 말고 하나의 흐름으로 이어지게 해 주세요.",
            "7. 장소가 누락된 경우에도 **상상력을 발휘하여 적절한 장소로 유추하거나 자연스럽게 생략**해 주세요.",
            "",
            "입력 형식은 아래 JSON 배열입니다:",
            "",
            "[",
            "  {",
            "    \"datetime\": \"2025-06-03T14:04:21\",",
            "    \"location\": \"서울 송파구 잠실동\",",
            "    \"description\": \"발코니에 서서 파란 건물을 바라보는 남성\"",
            "  },",
            "  {",
            "    \"datetime\": \"2025-06-03T17:20:00\",",
            "    \"location\": \"서울 강남구 신사동\",",
            "    \"description\": \"조용한 골목에서 아이스크림을 먹는 아이\"",
            "  }",
            "]",
            "",
            "💡 일기 제목은 하루의 감정을 압축적으로 표현해 주세요.",
            "예: `햇살이 닿은 마음`, `그늘 아래의 평온`, `조용히 지나간 하루`"
    );

    /**
     * 이미지 일기 항목들을 기반으로 GPT 프롬프트를 생성합니다.
     *
     * @param imageRecords 시간, 장소, 설명이 포함된 이미지 일기 항목 리스트
     * @return 완성된 GPT 프롬프트 문자열
     */
    public static String buildDiaryPrompt(List<ImageDiaryItem> imageRecords) {
        List<ImageDiaryItem> mutableRecords = new ArrayList<>(imageRecords);
        mutableRecords.sort(Comparator.comparing(ImageDiaryItem::getDatetime));

        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.registerModule(new JavaTimeModule());
            mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

            String jsonBody = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(mutableRecords);
            return String.join("\n\n", EMOTIONAL_DIARY_PROMPT_HEADER, jsonBody);
        } catch (Exception e) {
            throw new RuntimeException("GPT 프롬프트 생성 중 오류 발생", e);
        }
    }
}
