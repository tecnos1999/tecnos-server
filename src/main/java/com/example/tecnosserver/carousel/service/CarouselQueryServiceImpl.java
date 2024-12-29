package com.example.tecnosserver.carousel.service;

import com.example.tecnosserver.carousel.dto.CarouselDTO;
import com.example.tecnosserver.carousel.mapper.CarouselMapper;
import com.example.tecnosserver.carousel.repo.CarouselRepo;
import com.example.tecnosserver.exceptions.exception.AppException;
import com.example.tecnosserver.exceptions.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class CarouselQueryServiceImpl implements CarouselQueryService {

    private final CarouselRepo carouselRepo;
    private final CarouselMapper carouselMapper;

    @Override
    public Optional<List<CarouselDTO>> getAllCarouselItems() {
        try {
            log.info("Fetching all carousel items...");
            List<CarouselDTO> items = carouselRepo.findAll().stream()
                    .map(carouselMapper::toDTO)
                    .collect(Collectors.toList());

            if (items.isEmpty()) {
                log.warn("No carousel items found.");
                throw new NotFoundException("No carousel items found.");
            }

            log.info("Successfully fetched all carousel items.");
            return Optional.of(items);
        } catch (NotFoundException e) {
            log.error("Error: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("Failed to fetch all carousel items: {}", e.getMessage());
            throw new AppException("An unexpected error occurred while fetching carousel items.", e);
        }
    }

    @Override
    public Optional<List<CarouselDTO>> getAllImages() {
        try {
            log.info("Fetching all images from carousel...");
            List<CarouselDTO> images = carouselRepo.findAllImages()
                    .orElseThrow(() -> new NotFoundException("No images found in carousel."))
                    .stream()
                    .map(carouselMapper::toDTO)
                    .collect(Collectors.toList());

            log.info("Successfully fetched all images.");
            return Optional.of(images);
        } catch (NotFoundException e) {
            log.error("Error: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("Failed to fetch images from carousel: {}", e.getMessage());
            throw new AppException("An unexpected error occurred while fetching images.", e);
        }
    }

    @Override
    public Optional<List<CarouselDTO>> getAllVideos() {
        try {
            log.info("Fetching all videos from carousel...");
            List<CarouselDTO> videos = carouselRepo.findAllVideos()
                    .orElseThrow(() -> new NotFoundException("No videos found in carousel."))
                    .stream()
                    .map(carouselMapper::toDTO)
                    .collect(Collectors.toList());

            log.info("Successfully fetched all videos.");
            return Optional.of(videos);
        } catch (NotFoundException e) {
            log.error("Error: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("Failed to fetch videos from carousel: {}", e.getMessage());
            throw new AppException("An unexpected error occurred while fetching videos.", e);
        }
    }

    @Override
    public Optional<List<CarouselDTO>> getCarouselItemsOrderedByCreatedAt() {
        try {
            log.info("Fetching all carousel items ordered by creation date...");
            List<CarouselDTO> orderedItems = carouselRepo.findAllOrderByCreatedAtDesc()
                    .orElseThrow(() -> new NotFoundException("No carousel items found to order by creation date."))
                    .stream()
                    .map(carouselMapper::toDTO)
                    .collect(Collectors.toList());

            log.info("Successfully fetched carousel items ordered by creation date.");
            return Optional.of(orderedItems);
        } catch (NotFoundException e) {
            log.error("Error: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("Failed to fetch ordered carousel items: {}", e.getMessage());
            throw new AppException("An unexpected error occurred while fetching ordered carousel items.", e);
        }
    }
}
