package com.example.tecnosserver.carousel.web;

import com.example.tecnosserver.carousel.dto.CarouselDTO;
import com.example.tecnosserver.carousel.service.CarouselCommandService;
import com.example.tecnosserver.carousel.service.CarouselQueryService;
import com.example.tecnosserver.utils.AdvancedStructuredLogger;
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
@RequestMapping("/server/api/v1/carousel")
@RequiredArgsConstructor
public class CarouselControllerApi {

    private final CarouselCommandService carouselCommandService;
    private final CarouselQueryService carouselQueryService;
    private final AdvancedStructuredLogger advancedStructuredLogger;

    @PostMapping(value = "/create", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> addCarouselItem(
            @RequestPart("carousel") @Valid CarouselDTO carouselDTO,
            @RequestPart("file") MultipartFile file) {
        advancedStructuredLogger.logBuilder()
                .withEventType("CAROUSEL_ADD_START")
                .withMessage("Attempting to add carousel item")
                .withField("carouselDTO", carouselDTO)
                .withField("fileName", file.getOriginalFilename())
                .withLevel("INFO")
                .log();

        try {
            carouselCommandService.addCarouselItem(carouselDTO, file);
            advancedStructuredLogger.logBuilder()
                    .withEventType("CAROUSEL_ADD_SUCCESS")
                    .withMessage("Carousel item added successfully")
                    .withField("carouselDTO", carouselDTO)
                    .withLevel("INFO")
                    .log();
            return ResponseEntity.status(HttpStatus.CREATED).body("Carousel item added successfully.");
        } catch (Exception e) {
            advancedStructuredLogger.logBuilder()
                    .withEventType("CAROUSEL_ADD_ERROR")
                    .withMessage("Failed to add carousel item")
                    .withField("carouselDTO", carouselDTO)
                    .withField("error", e.getMessage())
                    .withLevel("ERROR")
                    .log();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to add carousel item: " + e.getMessage());
        }
    }

    @PutMapping(value = "/update/{code}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> updateCarouselItem(
            @PathVariable String code,
            @RequestPart("carousel") @Valid CarouselDTO carouselDTO,
            @RequestPart(value = "file", required = false) MultipartFile file) {
        advancedStructuredLogger.logBuilder()
                .withEventType("CAROUSEL_UPDATE_START")
                .withMessage("Attempting to update carousel item")
                .withField("code", code)
                .withField("carouselDTO", carouselDTO)
                .withField("fileName", file != null ? file.getOriginalFilename() : "No file provided")
                .withLevel("INFO")
                .log();

        try {
            carouselCommandService.updateCarouselItem(code, carouselDTO, file);
            advancedStructuredLogger.logBuilder()
                    .withEventType("CAROUSEL_UPDATE_SUCCESS")
                    .withMessage("Carousel item updated successfully")
                    .withField("code", code)
                    .withLevel("INFO")
                    .log();
            return ResponseEntity.ok("Carousel item updated successfully.");
        } catch (Exception e) {
            advancedStructuredLogger.logBuilder()
                    .withEventType("CAROUSEL_UPDATE_ERROR")
                    .withMessage("Failed to update carousel item")
                    .withField("code", code)
                    .withField("error", e.getMessage())
                    .withLevel("ERROR")
                    .log();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to update carousel item: " + e.getMessage());
        }
    }

    @DeleteMapping("/delete/{code}")
    public ResponseEntity<String> deleteCarouselItem(@PathVariable String code) {
        advancedStructuredLogger.logBuilder()
                .withEventType("CAROUSEL_DELETE_START")
                .withMessage("Attempting to delete carousel item")
                .withField("code", code)
                .withLevel("INFO")
                .log();

        try {
            carouselCommandService.deleteCarouselItem(code);
            advancedStructuredLogger.logBuilder()
                    .withEventType("CAROUSEL_DELETE_SUCCESS")
                    .withMessage("Carousel item deleted successfully")
                    .withField("code", code)
                    .withLevel("INFO")
                    .log();
            return ResponseEntity.ok("Carousel item deleted successfully.");
        } catch (Exception e) {
            advancedStructuredLogger.logBuilder()
                    .withEventType("CAROUSEL_DELETE_ERROR")
                    .withMessage("Failed to delete carousel item")
                    .withField("code", code)
                    .withField("error", e.getMessage())
                    .withLevel("ERROR")
                    .log();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to delete carousel item: " + e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<List<CarouselDTO>> getAllCarouselItems() {
        advancedStructuredLogger.logBuilder()
                .withEventType("CAROUSEL_FETCH_ALL_START")
                .withMessage("Fetching all carousel items")
                .withLevel("INFO")
                .log();

        try {
            Optional<List<CarouselDTO>> items = carouselQueryService.getAllCarouselItems();
            if (items.isEmpty() || items.get().isEmpty()) {
                advancedStructuredLogger.logBuilder()
                        .withEventType("CAROUSEL_FETCH_ALL_EMPTY")
                        .withMessage("No carousel items found")
                        .withLevel("WARN")
                        .log();
                return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
            }
            advancedStructuredLogger.logBuilder()
                    .withEventType("CAROUSEL_FETCH_ALL_SUCCESS")
                    .withMessage("Successfully fetched all carousel items")
                    .withField("itemCount", items.get().size())
                    .withLevel("INFO")
                    .log();
            return ResponseEntity.ok(items.get());
        } catch (Exception e) {
            advancedStructuredLogger.logBuilder()
                    .withEventType("CAROUSEL_FETCH_ALL_ERROR")
                    .withMessage("Failed to fetch all carousel items")
                    .withField("error", e.getMessage())
                    .withLevel("ERROR")
                    .log();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/images")
    public ResponseEntity<List<CarouselDTO>> getAllImages() {
        advancedStructuredLogger.logBuilder()
                .withEventType("CAROUSEL_FETCH_IMAGES_START")
                .withMessage("Fetching all carousel images")
                .withLevel("INFO")
                .log();

        try {
            Optional<List<CarouselDTO>> images = carouselQueryService.getAllImages();
            if (images.isEmpty() || images.get().isEmpty()) {
                advancedStructuredLogger.logBuilder()
                        .withEventType("CAROUSEL_FETCH_IMAGES_EMPTY")
                        .withMessage("No images found in carousel")
                        .withLevel("WARN")
                        .log();
                return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
            }
            advancedStructuredLogger.logBuilder()
                    .withEventType("CAROUSEL_FETCH_IMAGES_SUCCESS")
                    .withMessage("Successfully fetched all carousel images")
                    .withField("imageCount", images.get().size())
                    .withLevel("INFO")
                    .log();
            return ResponseEntity.ok(images.get());
        } catch (Exception e) {
            advancedStructuredLogger.logBuilder()
                    .withEventType("CAROUSEL_FETCH_IMAGES_ERROR")
                    .withMessage("Failed to fetch carousel images")
                    .withField("error", e.getMessage())
                    .withLevel("ERROR")
                    .log();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/videos")
    public ResponseEntity<List<CarouselDTO>> getAllVideos() {
        advancedStructuredLogger.logBuilder()
                .withEventType("CAROUSEL_FETCH_VIDEOS_START")
                .withMessage("Fetching all carousel videos")
                .withLevel("INFO")
                .log();

        try {
            Optional<List<CarouselDTO>> videos = carouselQueryService.getAllVideos();
            if (videos.isEmpty() || videos.get().isEmpty()) {
                advancedStructuredLogger.logBuilder()
                        .withEventType("CAROUSEL_FETCH_VIDEOS_EMPTY")
                        .withMessage("No videos found in carousel")
                        .withLevel("WARN")
                        .log();
                return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
            }
            advancedStructuredLogger.logBuilder()
                    .withEventType("CAROUSEL_FETCH_VIDEOS_SUCCESS")
                    .withMessage("Successfully fetched all carousel videos")
                    .withField("videoCount", videos.get().size())
                    .withLevel("INFO")
                    .log();
            return ResponseEntity.ok(videos.get());
        } catch (Exception e) {
            advancedStructuredLogger.logBuilder()
                    .withEventType("CAROUSEL_FETCH_VIDEOS_ERROR")
                    .withMessage("Failed to fetch carousel videos")
                    .withField("error", e.getMessage())
                    .withLevel("ERROR")
                    .log();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/ordered")
    public ResponseEntity<List<CarouselDTO>> getCarouselItemsOrderedByCreatedAt() {
        advancedStructuredLogger.logBuilder()
                .withEventType("CAROUSEL_FETCH_ORDERED_START")
                .withMessage("Fetching ordered carousel items")
                .withLevel("INFO")
                .log();

        try {
            Optional<List<CarouselDTO>> orderedItems = carouselQueryService.getCarouselItemsOrderedByCreatedAt();
            if (orderedItems.isEmpty() || orderedItems.get().isEmpty()) {
                advancedStructuredLogger.logBuilder()
                        .withEventType("CAROUSEL_FETCH_ORDERED_EMPTY")
                        .withMessage("No ordered carousel items found")
                        .withLevel("WARN")
                        .log();
                return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
            }
            advancedStructuredLogger.logBuilder()
                    .withEventType("CAROUSEL_FETCH_ORDERED_SUCCESS")
                    .withMessage("Successfully fetched ordered carousel items")
                    .withField("orderedItemCount", orderedItems.get().size())
                    .withLevel("INFO")
                    .log();
            return ResponseEntity.ok(orderedItems.get());
        } catch (Exception e) {
            advancedStructuredLogger.logBuilder()
                    .withEventType("CAROUSEL_FETCH_ORDERED_ERROR")
                    .withMessage("Failed to fetch ordered carousel items")
                    .withField("error", e.getMessage())
                    .withLevel("ERROR")
                    .log();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
