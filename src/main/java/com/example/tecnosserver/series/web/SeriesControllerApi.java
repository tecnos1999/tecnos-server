package com.example.tecnosserver.series.web;

import com.example.tecnosserver.series.dto.SeriesDTO;
import com.example.tecnosserver.series.service.SeriesCommandService;
import com.example.tecnosserver.series.service.SeriesQueryService;
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
@RequestMapping("/server/api/v1/series")
@RequiredArgsConstructor
public class SeriesControllerApi {

    private final SeriesCommandService seriesCommandService;
    private final SeriesQueryService seriesQueryService;

    @PostMapping(value = "/create", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> addSeries(
            @RequestPart("series") SeriesDTO seriesDTO,
            @RequestPart(value = "image", required = false)  MultipartFile image) {
        try {
            seriesCommandService.addSeries(seriesDTO, image);
            return ResponseEntity.status(HttpStatus.CREATED).body("Series added successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to create series: " + e.getMessage());
        }
    }

    @PutMapping(value = "/update/{code}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> updateSeries(
            @PathVariable String code,
            @RequestPart("series") SeriesDTO seriesDTO,
            @RequestPart(value = "image", required = false) MultipartFile image) {
        try {
            seriesCommandService.updateSeries(code, seriesDTO, image);
            return ResponseEntity.ok("Series updated successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to update series: " + e.getMessage());
        }
    }

    @DeleteMapping("/delete/{code}")
    public ResponseEntity<String> deleteSeries(@PathVariable String code) {
        try {
            seriesCommandService.deleteSeries(code);
            return ResponseEntity.ok("Series deleted successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to delete series: " + e.getMessage());
        }
    }

    @GetMapping("/{code}")
    public ResponseEntity<SeriesDTO> getSeriesByCode(@PathVariable String code) {
        return seriesQueryService.findSeriesByCode(code)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @GetMapping
    public ResponseEntity<List<SeriesDTO>> getAllSeries() {
        Optional<List<SeriesDTO>> seriesList = seriesQueryService.findAllSeries();
        if (seriesList.isEmpty() || seriesList.get().isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(seriesList.get());
    }
}
