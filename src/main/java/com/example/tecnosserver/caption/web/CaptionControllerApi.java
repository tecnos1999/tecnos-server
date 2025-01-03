package com.example.tecnosserver.caption.web;

import com.example.tecnosserver.caption.dto.CaptionDTO;
import com.example.tecnosserver.caption.service.CaptionCommandService;
import com.example.tecnosserver.caption.service.CaptionQueryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/server/api/v1/captions")
@RequiredArgsConstructor
public class CaptionControllerApi {

    private final CaptionCommandService captionCommandService;
    private final CaptionQueryService captionQueryService;

    @PostMapping(value = "/create", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> addCaption(
            @RequestPart("caption") @Valid CaptionDTO captionDTO,
            @RequestPart(value = "file", required = false) MultipartFile file) {
        captionCommandService.addCaption(captionDTO, file);
        return ResponseEntity.status(HttpStatus.CREATED).body("Caption added successfully.");
    }

    @PutMapping(value = "/update/{code}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> updateCaption(
            @PathVariable String code,
            @RequestPart("caption") @Valid CaptionDTO captionDTO,
            @RequestPart(value = "file", required = false) MultipartFile file) {
        try {
            captionCommandService.updateCaption(code, captionDTO, file);
            return ResponseEntity.ok("Caption updated successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to update caption: " + e.getMessage());
        }
    }

    @DeleteMapping("/delete/{code}")
    public ResponseEntity<String> deleteCaption(@PathVariable String code) {
        captionCommandService.deleteCaption(code);
        return ResponseEntity.ok("Caption deleted successfully.");
    }

    @GetMapping("/{code}")
    public ResponseEntity<CaptionDTO> getCaptionByCode(@PathVariable String code) {
        try {
            CaptionDTO captionDTO = captionQueryService.getCaptionByCode(code);
            return ResponseEntity.ok(captionDTO);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @GetMapping
    public ResponseEntity<List<CaptionDTO>> getAllCaptions() {
        List<CaptionDTO> captions = captionQueryService.getAllCaptions();
        return ResponseEntity.ok(captions);
    }
}
