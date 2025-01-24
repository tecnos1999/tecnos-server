package com.example.tecnosserver.carousel.service;

import com.example.tecnosserver.carousel.dto.CarouselDTO;
import com.example.tecnosserver.carousel.mapper.CarouselMapper;
import com.example.tecnosserver.carousel.repo.CarouselRepo;
import com.example.tecnosserver.exceptions.exception.AppException;
import com.example.tecnosserver.exceptions.exception.NotFoundException;
import com.example.tecnosserver.utils.AdvancedStructuredLogger;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CarouselQueryServiceImpl implements CarouselQueryService {

    private final CarouselRepo carouselRepo;
    private final CarouselMapper carouselMapper;
    private final AdvancedStructuredLogger advancedStructuredLogger;

    @Override
    public Optional<List<CarouselDTO>> getAllCarouselItems() {
        advancedStructuredLogger.logBuilder()
                .withEventType("CAROUSEL_QUERY_ALL_START")
                .withMessage("Fetching all carousel items")
                .withLevel("INFO")
                .log();

        try {
            List<CarouselDTO> items = carouselRepo.findAll().stream()
                    .map(carouselMapper::toDTO)
                    .collect(Collectors.toList());

            if (items.isEmpty()) {
                advancedStructuredLogger.logBuilder()
                        .withEventType("CAROUSEL_QUERY_ALL_EMPTY")
                        .withMessage("No carousel items found")
                        .withLevel("WARN")
                        .log();
                throw new NotFoundException("No carousel items found.");
            }

            advancedStructuredLogger.logBuilder()
                    .withEventType("CAROUSEL_QUERY_ALL_SUCCESS")
                    .withMessage("Successfully fetched all carousel items")
                    .withField("itemCount", items.size())
                    .withLevel("INFO")
                    .log();
            return Optional.of(items);
        } catch (NotFoundException e) {
            advancedStructuredLogger.logBuilder()
                    .withEventType("CAROUSEL_QUERY_ALL_NOT_FOUND")
                    .withMessage("Error fetching all carousel items")
                    .withField("error", e.getMessage())
                    .withLevel("ERROR")
                    .log();
            throw e;
        } catch (Exception e) {
            advancedStructuredLogger.logBuilder()
                    .withEventType("CAROUSEL_QUERY_ALL_EXCEPTION")
                    .withMessage("Unexpected error occurred while fetching all carousel items")
                    .withField("error", e.getMessage())
                    .withLevel("ERROR")
                    .log();
            throw new AppException("An unexpected error occurred while fetching carousel items.", e);
        }
    }

    @Override
    public Optional<List<CarouselDTO>> getAllImages() {
        advancedStructuredLogger.logBuilder()
                .withEventType("CAROUSEL_QUERY_IMAGES_START")
                .withMessage("Fetching all images from carousel")
                .withLevel("INFO")
                .log();

        try {
            List<CarouselDTO> images = carouselRepo.findAllImages()
                    .orElseThrow(() -> new NotFoundException("No images found in carousel."))
                    .stream()
                    .map(carouselMapper::toDTO)
                    .collect(Collectors.toList());

            advancedStructuredLogger.logBuilder()
                    .withEventType("CAROUSEL_QUERY_IMAGES_SUCCESS")
                    .withMessage("Successfully fetched all images from carousel")
                    .withField("imageCount", images.size())
                    .withLevel("INFO")
                    .log();
            return Optional.of(images);
        } catch (NotFoundException e) {
            advancedStructuredLogger.logBuilder()
                    .withEventType("CAROUSEL_QUERY_IMAGES_NOT_FOUND")
                    .withMessage("No images found in carousel")
                    .withField("error", e.getMessage())
                    .withLevel("ERROR")
                    .log();
            throw e;
        } catch (Exception e) {
            advancedStructuredLogger.logBuilder()
                    .withEventType("CAROUSEL_QUERY_IMAGES_EXCEPTION")
                    .withMessage("Unexpected error occurred while fetching images")
                    .withField("error", e.getMessage())
                    .withLevel("ERROR")
                    .log();
            throw new AppException("An unexpected error occurred while fetching images.", e);
        }
    }

    @Override
    public Optional<List<CarouselDTO>> getAllVideos() {
        advancedStructuredLogger.logBuilder()
                .withEventType("CAROUSEL_QUERY_VIDEOS_START")
                .withMessage("Fetching all videos from carousel")
                .withLevel("INFO")
                .log();

        try {
            List<CarouselDTO> videos = carouselRepo.findAllVideos()
                    .orElseThrow(() -> new NotFoundException("No videos found in carousel."))
                    .stream()
                    .map(carouselMapper::toDTO)
                    .collect(Collectors.toList());

            advancedStructuredLogger.logBuilder()
                    .withEventType("CAROUSEL_QUERY_VIDEOS_SUCCESS")
                    .withMessage("Successfully fetched all videos from carousel")
                    .withField("videoCount", videos.size())
                    .withLevel("INFO")
                    .log();
            return Optional.of(videos);
        } catch (NotFoundException e) {
            advancedStructuredLogger.logBuilder()
                    .withEventType("CAROUSEL_QUERY_VIDEOS_NOT_FOUND")
                    .withMessage("No videos found in carousel")
                    .withField("error", e.getMessage())
                    .withLevel("ERROR")
                    .log();
            throw e;
        } catch (Exception e) {
            advancedStructuredLogger.logBuilder()
                    .withEventType("CAROUSEL_QUERY_VIDEOS_EXCEPTION")
                    .withMessage("Unexpected error occurred while fetching videos")
                    .withField("error", e.getMessage())
                    .withLevel("ERROR")
                    .log();
            throw new AppException("An unexpected error occurred while fetching videos.", e);
        }
    }

    @Override
    public Optional<List<CarouselDTO>> getCarouselItemsOrderedByCreatedAt() {
        advancedStructuredLogger.logBuilder()
                .withEventType("CAROUSEL_QUERY_ORDERED_START")
                .withMessage("Fetching all carousel items ordered by creation date")
                .withLevel("INFO")
                .log();

        try {
            List<CarouselDTO> orderedItems = carouselRepo.findAllOrderByCreatedAtDesc()
                    .orElseThrow(() -> new NotFoundException("No carousel items found to order by creation date."))
                    .stream()
                    .map(carouselMapper::toDTO)
                    .collect(Collectors.toList());

            advancedStructuredLogger.logBuilder()
                    .withEventType("CAROUSEL_QUERY_ORDERED_SUCCESS")
                    .withMessage("Successfully fetched carousel items ordered by creation date")
                    .withField("orderedItemCount", orderedItems.size())
                    .withLevel("INFO")
                    .log();
            return Optional.of(orderedItems);
        } catch (NotFoundException e) {
            advancedStructuredLogger.logBuilder()
                    .withEventType("CAROUSEL_QUERY_ORDERED_NOT_FOUND")
                    .withMessage("No carousel items found to order by creation date")
                    .withField("error", e.getMessage())
                    .withLevel("ERROR")
                    .log();
            throw e;
        } catch (Exception e) {
            advancedStructuredLogger.logBuilder()
                    .withEventType("CAROUSEL_QUERY_ORDERED_EXCEPTION")
                    .withMessage("Unexpected error occurred while fetching ordered carousel items")
                    .withField("error", e.getMessage())
                    .withLevel("ERROR")
                    .log();
            throw new AppException("An unexpected error occurred while fetching ordered carousel items.", e);
        }
    }
}
