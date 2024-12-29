package com.example.tecnosserver.carousel.service;

import com.example.tecnosserver.carousel.dto.CarouselDTO;
import com.example.tecnosserver.carousel.mapper.CarouselMapper;
import com.example.tecnosserver.carousel.model.Carousel;
import com.example.tecnosserver.carousel.repo.CarouselRepo;
import com.example.tecnosserver.exceptions.exception.AppException;
import com.example.tecnosserver.exceptions.exception.NotFoundException;
import com.example.tecnosserver.intercom.CloudAdapter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.Objects;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class CarouselCommandServiceImpl implements CarouselCommandService {

    private final CarouselRepo carouselRepo;
    private final CarouselMapper carouselMapper;
    private final CloudAdapter cloudAdapter;

    @Override
    public void addCarouselItem(CarouselDTO carouselDTO, MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new AppException("File cannot be null or empty.");
        }

        try {
            String fileUrl = uploadFile(file, "Failed to upload file: ");
            Carousel carousel = carouselMapper.fromDTO(
                    new CarouselDTO(null, fileUrl, determineFileType(file), null, null)
            );

            carouselRepo.save(carousel);
            log.info("Carousel item added successfully: {}", carousel);
        } catch (Exception e) {
            log.error("Error adding carousel item: {}", e.getMessage());
            throw new AppException("An error occurred while adding the carousel item.", e);
        }
    }

    @Override
    public void updateCarouselItem(String code, CarouselDTO carouselDTO, MultipartFile file) {
        try {
            Carousel carousel = carouselRepo.findByCode(code)
                    .orElseThrow(() -> new NotFoundException("Carousel item with code '" + code + "' not found."));

            if (file != null && !file.isEmpty()) {
                safeDeleteFile(carousel.getFileUrl());
                String fileUrl = uploadFile(file, "Failed to upload new file: ");
                carousel.setFileUrl(fileUrl);
                carousel.setType(determineFileType(file));
            }

             carouselRepo.save(carousel);
            log.info("Carousel item updated successfully: {}", carousel);
        } catch (NotFoundException e) {
            log.error("Error updating carousel item: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("Error updating carousel item: {}", e.getMessage());
            throw new AppException("An error occurred while updating the carousel item.", e);
        }
    }

    @Override
    public void deleteCarouselItem(String code) {
        try {
            Carousel carousel = carouselRepo.findByCode(code)
                    .orElseThrow(() -> new NotFoundException("Carousel item with code '" + code + "' not found."));

            safeDeleteFile(carousel.getFileUrl());
            carouselRepo.delete(carousel);
            log.info("Carousel item deleted successfully: {}", code);
        } catch (NotFoundException e) {
            log.error("Error deleting carousel item: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("Error deleting carousel item: {}", e.getMessage());
            throw new AppException("An error occurred while deleting the carousel item.", e);
        }
    }

    private String uploadFile(MultipartFile file, String errorMessage) {
        try {
            return cloudAdapter.uploadFile(file);
        } catch (Exception e) {
            log.error("{} {}", errorMessage, e.getMessage());
            throw new AppException(errorMessage + e.getMessage());
        }
    }

    private void safeDeleteFile(String fileUrl) {
        if (fileUrl != null && !fileUrl.isBlank()) {
            try {
                cloudAdapter.deleteFile(fileUrl);
                log.info("Deleted file from cloud: {}", fileUrl);
            } catch (Exception e) {
                log.error("Failed to delete associated file: {}", e.getMessage());
                throw new AppException("Failed to delete associated file: " + e.getMessage(), e);
            }
        }
    }

    private String determineFileType(MultipartFile file) {
        try {
            return Objects.requireNonNull(file.getContentType()).contains("image") ? "image" : "video";
        } catch (Exception e) {
            log.error("Failed to determine file type: {}", e.getMessage());
            throw new AppException("Unable to determine file type.", e);
        }
    }
}