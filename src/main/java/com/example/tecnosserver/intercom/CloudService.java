package com.example.tecnosserver.intercom;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(name = "tecnos-cloud", url = "${gateway.url}")
public interface CloudService {

    @DeleteMapping("/cloud/document/files/batch")
    ResponseEntity<String> deleteFiles(@RequestBody List<String> fileUrls);
}
