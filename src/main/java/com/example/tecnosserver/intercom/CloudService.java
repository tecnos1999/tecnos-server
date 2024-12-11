package com.example.tecnosserver.intercom;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@FeignClient(name = "tecnos-cloud", url = "${gateway.url}")
public interface CloudService {

    @DeleteMapping("/cloud/document/files/batch")
    ResponseEntity<String> deleteFiles(@RequestBody List<String> fileUrls);

    @DeleteMapping("/cloud/document/files")
    ResponseEntity<String> deleteFile(@RequestParam("fileUrl") String fileUrl);

    @PostMapping(value = "/cloud/document/uploadSingle", consumes = "multipart/form-data")
    ResponseEntity<String> uploadFile(@RequestPart("file") MultipartFile file);

    @PostMapping(value = "/cloud/document/upload", consumes = "multipart/form-data")
    ResponseEntity<List<String>> uploadMultipleFiles(@RequestPart("files") List<MultipartFile> files);

    @GetMapping("/cloud/document/files/{filename}")
    ResponseEntity<byte[]> getFile(@PathVariable("filename") String filename);
}
