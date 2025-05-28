package com.photodiary.backend.service;

import org.springframework.core.io.FileSystemResource;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ImageProcessService {

    private final RestTemplate restTemplate;

    public ImageProcessService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public Map<String, Object> sendImagesToFastApi(List<MultipartFile> images, String privacy) throws IOException {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();

        for (MultipartFile image : images) {
            File file = convertToFile(image);
            body.add("files", new FileSystemResource(file));
        }

        body.add("privacy", privacy);

        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

        String fastApiUrl = "http://localhost:8000/process-images";
        ResponseEntity<Map> response = restTemplate.postForEntity(fastApiUrl, requestEntity, Map.class);

        Map<String, Object> fastApiResult = response.getBody();
        if (fastApiResult == null) {
            System.out.println("fastApiResult가 비어있습니다.");
            fastApiResult = new HashMap<>();
        }

        Map<String, Object> result = new HashMap<>();
        result.put("success", true);
        result.put("message", "이미지가 성공적으로 처리되었습니다.");
        result.put("imageCount", images.size());
        result.put("privacy", privacy);
        result.put("content", fastApiResult.getOrDefault("results", "일기 내용이 비어있습니다."));

        return result;
    }

    private File convertToFile(MultipartFile multipartFile) throws IOException {
        File convFile = File.createTempFile("upload_", "_" + multipartFile.getOriginalFilename());
        try (FileOutputStream fos = new FileOutputStream(convFile)) {
            fos.write(multipartFile.getBytes());
        }
        return convFile;
    }
}
