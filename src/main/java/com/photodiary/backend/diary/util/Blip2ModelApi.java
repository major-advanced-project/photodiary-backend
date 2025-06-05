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

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@Component
public class Blip2ModelApi {
    private RestTemplate restTemplate;
    private String fastApiUrl = "http://localhost:8000/process-images";

    public Blip2ModelApi() {
        this.restTemplate = new RestTemplate();
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

        System.out.println(response.getResults().get(0));

        return new ArrayList<>(response.getResults());
    }
}
