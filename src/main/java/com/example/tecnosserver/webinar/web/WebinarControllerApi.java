package com.example.tecnosserver.webinar.web;

import com.example.tecnosserver.webinar.dto.WebinarDTO;
import com.example.tecnosserver.webinar.service.WebinarCommandService;
import com.example.tecnosserver.webinar.service.WebinarQueryService;
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
@RequestMapping("/server/api/v1/webinars")
@RequiredArgsConstructor
public class WebinarControllerApi {

    private final WebinarCommandService webinarCommandService;
    private final WebinarQueryService webinarQueryService;


    @PostMapping(value = "/create", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> addWebinar(
            @RequestPart("webinar") WebinarDTO webinarDTO,
            @RequestPart(value = "image", required = false) MultipartFile image) {
        try {
            webinarCommandService.addWebinar(webinarDTO, image);
            return ResponseEntity.status(HttpStatus.CREATED).body("Webinar added successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to create webinar: " + e.getMessage());
        }
    }

    @PutMapping(value = "/update", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> updateWebinar(
            @RequestPart("webinar") WebinarDTO webinarDTO,
            @RequestPart(value = "image", required = false) MultipartFile image) {
        try {
            webinarCommandService.updateWebinar(webinarDTO, image);
            return ResponseEntity.ok("Webinar updated successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to update webinar: " + e.getMessage());
        }
    }


    @DeleteMapping("/delete/{webCode}")
    public ResponseEntity<String> deleteWebinar(@PathVariable String webCode) {
        webinarCommandService.deleteWebinar(webCode);
        return ResponseEntity.ok("Webinar deleted successfully.");
    }


    @GetMapping("/{webCode}")
    public ResponseEntity<WebinarDTO> getWebinarByCode(@PathVariable String webCode) {
        return webinarQueryService.findWebinarByCode(webCode)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @GetMapping
    public ResponseEntity<List<WebinarDTO>> getAllWebinars() {
        Optional<List<WebinarDTO>> webinars = webinarQueryService.findAllWebinars();
        if (webinars.isEmpty() || webinars.get().isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(webinars.get());
    }
}
