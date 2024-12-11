package com.example.tecnosserver.intercom;


import com.example.tecnosserver.exceptions.exception.AppException;
import com.example.tecnosserver.intercom.dto.UploadResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public class CloudAdapter {

    private final CloudService cloudService;

    public CloudAdapter(CloudService cloudService) {
        this.cloudService = cloudService;
    }

    public String deleteFiles(List<String> fileUrls) {
        ResponseEntity<String> response = cloudService.deleteFiles(fileUrls);
        return response.getBody();
    }

    public String uploadFile(MultipartFile image) {
        ResponseEntity<String> response = cloudService.uploadFile(image);

        if (response.getStatusCode().is2xxSuccessful()) {
            try {
                ObjectMapper objectMapper = new ObjectMapper();
                UploadResponse uploadResponse = objectMapper.readValue(response.getBody(), UploadResponse.class);

                return uploadResponse.getUrl();
            } catch (JsonProcessingException e) {
                throw new AppException("Failed to parse upload response: " + e.getMessage());
            }
        } else {
            throw new AppException("Failed to upload image: " + response.getBody());
        }
    }


    public void deleteFile(String url) {
        ResponseEntity<String> response = cloudService.deleteFile(url);
        response.getBody();
    }
}
