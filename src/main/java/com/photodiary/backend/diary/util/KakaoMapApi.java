package com.photodiary.backend.diary.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Component
public class KakaoMapApi {
    @Value("${kakao.api.key}")
    private String kakaoApiKey;
    private RestTemplate rest;
    public KakaoMapApi() {
        this.rest = new RestTemplate();
        // 생성자에서는 Value에 의해 값이 쓰이기 전임
    }

    public String retrievePlaceName(GpsCoordinate gpsPoint) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "KakaoAK " + kakaoApiKey);
        HttpEntity<String> entity = new HttpEntity<>(headers);
        String url = "https://dapi.kakao.com/v2/local/geo/coord2address.json?x=" + gpsPoint.longitude + "&y=" + gpsPoint.latitude;

        ResponseEntity<String> res = rest.exchange(url, HttpMethod.GET, entity, String.class);

        log.info("[retrievePlaceName] response = " + res.getBody());

        try {
            return parseRegion3depthName(res);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    private static String parseRegion3depthName(ResponseEntity<String> res) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode root = mapper.readTree(res.getBody());
        String region3 = root.path("documents").get(0).path("address").path("region_3depth_name").asText();
        return region3;
    }
}
