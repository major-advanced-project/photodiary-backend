package com.photodiary.backend.diary.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
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

    private final RestTemplate rest;

    public KakaoMapApi() {
        this.rest = new RestTemplate();
    }

    /**
     * 위도/경도를 입력받아 장소 관련 전체 주소 정보를 JSON string으로 반환합니다.
     */
    public String retrieveAddressJson(GpsCoordinate gpsPoint) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "KakaoAK " + kakaoApiKey);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        String url = "https://dapi.kakao.com/v2/local/geo/coord2address.json?x=" +
                gpsPoint.longitude + "&y=" + gpsPoint.latitude;

        ResponseEntity<String> res = rest.exchange(url, HttpMethod.GET, entity, String.class);

        log.info("[retrieveAddressJson] response = {}", res.getBody());

        try {
            return extractBestPlaceOnlyJson(res);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("주소 정보 파싱 실패", e);
        }
    }

    /**
     * Kakao API 응답에서 주요 주소 항목만 뽑아 JSON 문자열로 반환
     */
    private static String extractBestPlaceOnlyJson(ResponseEntity<String> res) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode root = mapper.readTree(res.getBody());
        JsonNode doc = root.path("documents").get(0);

        JsonNode address = doc.path("address");
        JsonNode road = doc.path("road_address");

        String bestPlaceName = "";

        if (!road.path("building_name").asText().isEmpty()) {
            bestPlaceName = road.path("building_name").asText();
        } else if (!road.path("address_name").asText().isEmpty()) {
            bestPlaceName = road.path("address_name").asText();
        } else if (!address.path("address_name").asText().isEmpty()) {
            bestPlaceName = address.path("address_name").asText();
        } else if (!address.path("region_3depth_name").asText().isEmpty()) {
            bestPlaceName = address.path("region_3depth_name").asText();
        } else {
            bestPlaceName = "이름 없는 장소";
        }

        ObjectNode result = mapper.createObjectNode();
        result.put("bestPlaceName", bestPlaceName);

        return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(result);
    }


    public static String formatPlaceName(JsonNode placeJson) {
        String building = placeJson.path("road_address_buildingName").asText();
        String road = placeJson.path("road_address_addressName").asText();
        String lot = placeJson.path("address_addressName").asText();
        String region3 = placeJson.path("address_region3depthName").asText();

        if (!building.isEmpty()) return building;
        if (!road.isEmpty()) return road;
        if (!lot.isEmpty()) return lot;
        if (!region3.isEmpty()) return region3;
        return "이름 없는 장소";
    }


}
