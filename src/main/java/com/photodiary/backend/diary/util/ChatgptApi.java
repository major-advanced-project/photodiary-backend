package com.photodiary.backend.diary.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.photodiary.backend.diary.dto.DiaryTitleAndContent;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class ChatgptApi {

    @Value("${openai.api-key}")
    private String apiKey;

    private final String endpoint = "https://api.openai.com/v1/chat/completions";
    private final RestTemplate restTemplate = new RestTemplate();

    public DiaryTitleAndContent retrieveDiary(String prompt) {
        Map<String, Object> requestBody = Map.of(
                "model", "gpt-4o",
                "messages", List.of(Map.of("role", "user", "content", prompt))
        );

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(apiKey);

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(requestBody, headers);

        try {
            ResponseEntity<String> response = restTemplate.postForEntity(endpoint, request, String.class);

            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(response.getBody());
            String content = root.path("choices").get(0).path("message").path("content").asText();

            // 제목/내용 분리
            String[] lines = content.split("\n", 2); // 첫 줄과 나머지 줄 분리
            String title = lines[0].replace("제목:", "").trim();
            String body = lines[1].replace("내용:", "").trim();

            return new DiaryTitleAndContent(title, body);
        } catch (Exception e) {
            throw new RuntimeException("ChatGPT 호출 실패", e);
        }
    }

}
