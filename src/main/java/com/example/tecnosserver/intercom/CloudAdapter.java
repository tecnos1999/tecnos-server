package com.example.tecnosserver.intercom;


import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

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

}
