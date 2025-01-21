package com.example.tecnosserver.page.web;

import com.example.tecnosserver.page.dto.CreatePageDTO;
import com.example.tecnosserver.page.dto.PageResponseDTO;
import com.example.tecnosserver.page.service.PageCommandService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/server/api/v1/pages")
@RequiredArgsConstructor
public class PageControllerApi {

    private final PageCommandService pageCommandService;
    private final ObjectMapper objectMapper;

    @PostMapping(value = "/create", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<PageResponseDTO> createPage(
            @RequestPart("pageDTO") String pageDTOJson,
            @RequestPart(value = "pageImage", required = false) MultipartFile pageImage,
            @RequestPart(value = "sectionImages", required = false) MultipartFile[] sectionImages) {
        try {
            CreatePageDTO createPageDTO = objectMapper.readValue(pageDTOJson, CreatePageDTO.class);

            Map<Integer, MultipartFile> sectionImagesMap = new HashMap<>();
            if (sectionImages != null) {
                for (int i = 0; i < sectionImages.length; i++) {
                    sectionImagesMap.put(i, sectionImages[i]);
                }
            }

            PageResponseDTO response = pageCommandService.createPage(createPageDTO, pageImage, sectionImagesMap);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            throw new RuntimeException("Failed to create page: " + e.getMessage());
        }
    }

    @PutMapping("/update/{slug}")
    public ResponseEntity<String> updatePage(
            @PathVariable String slug,
            @RequestParam("pageDTO") String pageDTOJson,
            @RequestParam(value = "pageImage", required = false) MultipartFile pageImage,
            @RequestParam(value = "sectionImages", required = false) MultipartFile[] sectionImages) {
        try {
            CreatePageDTO createPageDTO = objectMapper.readValue(pageDTOJson, CreatePageDTO.class);

            Map<Integer, MultipartFile> sectionImagesMap = new HashMap<>();
            if (sectionImages != null) {
                for (int i = 0; i < sectionImages.length; i++) {
                    sectionImagesMap.put(i, sectionImages[i]);
                }
            }

            pageCommandService.updatePage(slug, createPageDTO, pageImage, sectionImagesMap);
            return ResponseEntity.ok("Page updated successfully");
        } catch (Exception e) {
            throw new RuntimeException("Failed to update page: " + e.getMessage());
        }
    }

    @DeleteMapping("/delete/{slug}")
    public ResponseEntity<String> deletePage(@PathVariable String slug) {
        pageCommandService.deletePage(slug);
        return ResponseEntity.ok("Page deleted successfully");
    }
}
