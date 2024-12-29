package com.example.tecnosserver.carousel.web;

import com.example.tecnosserver.carousel.dto.CarouselDTO;
import com.example.tecnosserver.carousel.service.CarouselCommandService;
import com.example.tecnosserver.carousel.service.CarouselQueryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin
@RequestMapping("/server/api/v1/carousel")
@RequiredArgsConstructor
@Slf4j
public class CarouselControllerApi {

    private final CarouselCommandService carouselCommandService;
    private final CarouselQueryService carouselQueryService;

    @PostMapping(value = "/create", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> addCarouselItem(
            @RequestPart("carousel") @Valid CarouselDTO carouselDTO,
            @RequestPart("file") MultipartFile file) {
        try {
            carouselCommandService.addCarouselItem(carouselDTO, file);
            log.info("Carousel item added successfully.");
            return ResponseEntity.status(HttpStatus.CREATED).body("Carousel item added successfully.");
        } catch (Exception e) {
            log.error("Failed to add carousel item: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to add carousel item: " + e.getMessage());
        }
    }

    @PutMapping(value = "/update/{code}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> updateCarouselItem(
            @PathVariable String code,
            @RequestPart("carousel") @Valid CarouselDTO carouselDTO,
            @RequestPart(value = "file", required = false) MultipartFile file) {
        log.info("Received update request for code: {}", code);
        log.info("CarouselDTO: {}", carouselDTO);
        log.info("File: {}", file != null ? file.getOriginalFilename() : "No file provided");

        try {
            carouselCommandService.updateCarouselItem(code, carouselDTO, file);
            log.info("Carousel item with code {} updated successfully.", code);
            return ResponseEntity.ok("Carousel item updated successfully.");
        } catch (Exception e) {
            log.error("Failed to update carousel item with code {}: {}", code, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to update carousel item: " + e.getMessage());
        }
    }

    @DeleteMapping("/delete/{code}")
    public ResponseEntity<String> deleteCarouselItem(@PathVariable String code) {
        try {
            carouselCommandService.deleteCarouselItem(code);
            log.info("Carousel item with code {} deleted successfully.", code);
            return ResponseEntity.ok("Carousel item deleted successfully.");
        } catch (Exception e) {
            log.error("Failed to delete carousel item with code {}: {}", code, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to delete carousel item: " + e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<List<CarouselDTO>> getAllCarouselItems() {
        try {
            Optional<List<CarouselDTO>> items = carouselQueryService.getAllCarouselItems();
            if (items.isEmpty() || items.get().isEmpty()) {
                log.warn("No carousel items found.");
                return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
            }
            log.info("Successfully fetched all carousel items.");
            return ResponseEntity.ok(items.get());
        } catch (Exception e) {
            log.error("Failed to fetch carousel items: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/images")
    public ResponseEntity<List<CarouselDTO>> getAllImages() {
        try {
            Optional<List<CarouselDTO>> images = carouselQueryService.getAllImages();
            if (images.isEmpty() || images.get().isEmpty()) {
                log.warn("No images found in carousel.");
                return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
            }
            log.info("Successfully fetched all carousel images.");
            return ResponseEntity.ok(images.get());
        } catch (Exception e) {
            log.error("Failed to fetch carousel images: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/videos")
    public ResponseEntity<List<CarouselDTO>> getAllVideos() {
        try {
            Optional<List<CarouselDTO>> videos = carouselQueryService.getAllVideos();
            if (videos.isEmpty() || videos.get().isEmpty()) {
                log.warn("No videos found in carousel.");
                return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
            }
            log.info("Successfully fetched all carousel videos.");
            return ResponseEntity.ok(videos.get());
        } catch (Exception e) {
            log.error("Failed to fetch carousel videos: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/ordered")
    public ResponseEntity<List<CarouselDTO>> getCarouselItemsOrderedByCreatedAt() {
        try {
            Optional<List<CarouselDTO>> orderedItems = carouselQueryService.getCarouselItemsOrderedByCreatedAt();
            if (orderedItems.isEmpty() || orderedItems.get().isEmpty()) {
                log.warn("No ordered carousel items found.");
                return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
            }
            log.info("Successfully fetched ordered carousel items.");
            return ResponseEntity.ok(orderedItems.get());
        } catch (Exception e) {
            log.error("Failed to fetch ordered carousel items: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
