package com.photodiary.backend.diary.util;

import com.photodiary.backend.diary.dto.Blip2ProcessResponse;
import com.photodiary.backend.diary.dto.ImageDiaryItem;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.client.SimpleClientHttpRequestFactory;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@Component
public class Blip2ModelApi {

    private final RestTemplate restTemplate;
    private final String fastApiUrl = "https://a72c-1-240-46-14.ngrok-free.app/process-images";

    public Blip2ModelApi() {
        // 타임아웃 설정이 포함된 RestTemplate 직접 생성
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(10000); // 연결 대기 최대 10초
        factory.setReadTimeout(30000);    // 응답 대기 최대 30초
        this.restTemplate = new RestTemplate(factory);
    }

    public List<ImageDiaryItem> retreiveImageDescirptions(List<File> images) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        for (File image : images) {
            body.add("files", new FileSystemResource(image));
        }

        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);
        Blip2ProcessResponse response = restTemplate.postForEntity(fastApiUrl, requestEntity, Blip2ProcessResponse.class).getBody();

        return new ArrayList<>(response.getResults());
    }
}
