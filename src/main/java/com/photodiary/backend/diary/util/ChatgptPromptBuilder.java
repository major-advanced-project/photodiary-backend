package com.photodiary.backend.diary.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.photodiary.backend.diary.dto.ImageWithDescription;

import java.util.Comparator;
import java.util.List;

public class ChatgptPromptBuilder {

    private static final String EMOTIONAL_DIARY_PROMPT_HEADER = String.join("\n\n",
            "다음은 어떤 하루를 기록한 사진들입니다. 각 항목에는 시간, 장소, 그리고 그 순간을 묘사한 설명이 담겨 있습니다.",
            "이 정보를 바탕으로, 한 사람이 하루를 되돌아보며 작성한 감정 중심의 일기를 만들어 주세요.",
            "각 장면을 단순히 나열하지 말고, 시간의 흐름에 따라 장소가 바뀌고 감정도 자연스럽게 변화하는 듯한 글이 되면 좋겠습니다.",
            "사진 속 풍경을 보며 어떤 기분이 들었을지 상상해서 주관적인 감정을 담아 주세요.",
            "슬픔, 설렘, 평온함, 고요함, 그리움 등 다양한 감정이 담길 수 있습니다.",
            "너무 딱딱하거나 설명식이 되지 않도록, 자연스럽고 사람다운 언어로 작성해 주세요.",
            "입력은 아래 JSON입니다:"
    );

    public static String buildDiaryPrompt(List<ImageWithDescription> imageRecords) {

        //{파일명,설명,시간,장소}로 돼있는 LIST를 시간 기준으로 나눔
//        imageRecords.sort(Comparator.comparing(ImageWithDescription::getDatetime));

        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.registerModule(new JavaTimeModule());
            mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

            String jsonBody = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(imageRecords);

            return String.join("\n\n", EMOTIONAL_DIARY_PROMPT_HEADER, jsonBody);
        } catch (Exception e) {
            throw new RuntimeException("GPT 프롬프트 생성 중 오류 발생", e);
        }
    }
}
