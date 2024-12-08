package com.example.tecnosserver.webinar.web;

import com.example.tecnosserver.webinar.dto.WebinarDTO;
import com.example.tecnosserver.webinar.service.WebinarCommandService;
import com.example.tecnosserver.webinar.service.WebinarQueryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin
@RequestMapping("/server/api/v1/webinars")
@RequiredArgsConstructor
public class WebinarControllerApi {

    private final WebinarCommandService webinarCommandService;
    private final WebinarQueryService webinarQueryService;


    @PostMapping("/create")
    public ResponseEntity<String> addWebinar(@Valid @RequestBody WebinarDTO webinarDTO) {
        webinarCommandService.addWebinar(webinarDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body("Webinar added successfully.");
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
