package com.example.tecnosserver.motto.web;


import com.example.tecnosserver.motto.dto.MottoDTO;
import com.example.tecnosserver.motto.service.MottoCommandService;
import com.example.tecnosserver.motto.service.MottoQueryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin
@RequestMapping("/server/api/v1/motto")
@RequiredArgsConstructor
@Slf4j
public class MottoControllerApi {

    private final MottoCommandService mottoCommandService;
    private final MottoQueryService mottoQueryService;

    @PostMapping("/create")
    public ResponseEntity<String> addMotto(@RequestBody @Valid MottoDTO mottoDTO) {
        try {
            mottoCommandService.addMotto(mottoDTO);
            log.info("Motto added successfully.");
            return ResponseEntity.status(HttpStatus.CREATED).body("Motto added successfully.");
        } catch (Exception e) {
            log.error("Failed to add motto: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to add motto: " + e.getMessage());
        }
    }

    @PutMapping("/update/{code}")
    public ResponseEntity<String> updateMotto(@PathVariable String code, @RequestBody @Valid MottoDTO mottoDTO) {
        try {
            mottoCommandService.updateMotto(code, mottoDTO);
            log.info("Motto with code {} updated successfully.", code);
            return ResponseEntity.ok("Motto updated successfully.");
        } catch (Exception e) {
            log.error("Failed to update motto with code {}: {}", code, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to update motto: " + e.getMessage());
        }
    }

    @DeleteMapping("/delete/{code}")
    public ResponseEntity<String> deleteMotto(@PathVariable String code) {
        try {
            mottoCommandService.deleteMotto(code);
            log.info("Motto with code {} deleted successfully.", code);
            return ResponseEntity.ok("Motto deleted successfully.");
        } catch (Exception e) {
            log.error("Failed to delete motto with code {}: {}", code, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to delete motto: " + e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<List<MottoDTO>> getAllMottos() {
        try {
            Optional<List<MottoDTO>> mottos = mottoQueryService.getAllMottos();
            if (mottos.isEmpty() || mottos.get().isEmpty()) {
                log.warn("No mottos found.");
                return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
            }
            log.info("Successfully fetched all mottos.");
            return ResponseEntity.ok(mottos.get());
        } catch (Exception e) {
            log.error("Failed to fetch mottos: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/{code}")
    public ResponseEntity<MottoDTO> getMottoByCode(@PathVariable String code) {
        try {
            Optional<MottoDTO> motto = mottoQueryService.getMottoByCode(code);
            if (motto.isEmpty()) {
                log.warn("Motto with code {} not found.", code);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }
            log.info("Successfully fetched motto with code {}.", code);
            return ResponseEntity.ok(motto.get());
        } catch (Exception e) {
            log.error("Failed to fetch motto with code {}: {}", code, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}

