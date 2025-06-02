package com.photodiary.backend.diary.util;

import com.photodiary.backend.diary.dto.Blip2ProcessResponse;
import com.photodiary.backend.diary.dto.ImageWithDescription;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

@Component
public class Blip2ModelApi {
    private RestTemplate restTemplate;
    private String fastApiUrl = "http://localhost:8000/process-images";

    public Blip2ModelApi() {
        this.restTemplate = new RestTemplate();
    }

    public List<ImageWithDescription> retreiveImageDescirptions(List<MultipartFile> images) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();

        for (MultipartFile image : images) {
            File file = null;
            try {
                file = convertToFile(image);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            body.add("files", new FileSystemResource(file));
        }

        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);
        Blip2ProcessResponse response = restTemplate.postForEntity(fastApiUrl, requestEntity, Blip2ProcessResponse.class).getBody();

        System.out.println(response.getResults().get(0));

        return response.getResults();
    }

    private File convertToFile(MultipartFile multipartFile) throws IOException {
        File convFile = File.createTempFile("upload_", "_" + multipartFile.getOriginalFilename());
        try (FileOutputStream fos = new FileOutputStream(convFile)) {
            fos.write(multipartFile.getBytes());
        }
        return convFile;
    }
}
