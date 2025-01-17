package com.example.tecnosserver.sections.web;

import com.example.tecnosserver.sections.dto.SectionDTO;
import com.example.tecnosserver.sections.service.SectionCommandService;
import com.example.tecnosserver.sections.service.SectionQueryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin
@RequestMapping("/server/api/v1/sections")
@RequiredArgsConstructor
public class SectionControllerApi {

    private final SectionCommandService sectionCommandService;
    private final SectionQueryService sectionQueryService;

    @PostMapping(value = "/create", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> addSection(
            @RequestPart("section") @Valid SectionDTO sectionDTO,
            @RequestPart(value = "image", required = false) MultipartFile image) {
        try {
            sectionCommandService.addSection(sectionDTO, image);
            return ResponseEntity.status(HttpStatus.CREATED).body("Section added successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to create section: " + e.getMessage());
        }
    }

    @PutMapping(value = "/update/{sectionId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> updateSection(
            @PathVariable Long sectionId,
            @RequestPart("section") @Valid SectionDTO sectionDTO,
            @RequestPart(value = "image", required = false) MultipartFile image) {
        try {
            sectionCommandService.updateSection(sectionId, sectionDTO, image);
            return ResponseEntity.ok("Section updated successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to update section: " + e.getMessage());
        }
    }

    @DeleteMapping("/delete/{sectionId}")
    public ResponseEntity<String> deleteSection(@PathVariable Long sectionId) {
        try {
            sectionCommandService.deleteSection(sectionId);
            return ResponseEntity.ok("Section deleted successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to delete section: " + e.getMessage());
        }
    }

    @GetMapping("/{sectionId}")
    public ResponseEntity<SectionDTO> getSectionById(@PathVariable Long sectionId) {
        return sectionQueryService.findSectionById(sectionId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @GetMapping("/page/{pageId}")
    public ResponseEntity<List<SectionDTO>> getSectionsByPageId(@PathVariable Long pageId) {
        Optional<List<SectionDTO>> sections = sectionQueryService.findSectionsByPageId(pageId);
        if (sections.isEmpty() || sections.get().isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(sections.get());
    }

    @GetMapping
    public ResponseEntity<List<SectionDTO>> getAllSections() {
        Optional<List<SectionDTO>> sections = sectionQueryService.findAllSections();
        if (sections.isEmpty() || sections.get().isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(sections.get());
    }
}

