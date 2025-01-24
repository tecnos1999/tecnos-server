package com.example.tecnosserver.carousel.service;

import com.example.tecnosserver.carousel.dto.CarouselDTO;
import com.example.tecnosserver.carousel.mapper.CarouselMapper;
import com.example.tecnosserver.carousel.model.Carousel;
import com.example.tecnosserver.carousel.repo.CarouselRepo;
import com.example.tecnosserver.exceptions.exception.AppException;
import com.example.tecnosserver.exceptions.exception.NotFoundException;
import com.example.tecnosserver.intercom.CloudAdapter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import com.example.tecnosserver.utils.AdvancedStructuredLogger;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Transactional
public class CarouselCommandServiceImpl implements CarouselCommandService {

    private final CarouselRepo carouselRepo;
    private final CarouselMapper carouselMapper;
    private final CloudAdapter cloudAdapter;
    private final AdvancedStructuredLogger advancedStructuredLogger;

    @Override
    public void addCarouselItem(CarouselDTO carouselDTO, MultipartFile file) {
        advancedStructuredLogger.logBuilder()
                .withEventType("CAROUSEL_ADD_START")
                .withMessage("Attempting to add carousel item")
                .withField("carouselDTO", carouselDTO)
                .withLevel("INFO")
                .log();

        if (file == null || file.isEmpty()) {
            advancedStructuredLogger.logBuilder()
                    .withEventType("CAROUSEL_ADD_ERROR")
                    .withMessage("File is null or empty")
                    .withLevel("ERROR")
                    .log();
            throw new AppException("File cannot be null or empty.");
        }

        try {
            String fileUrl = uploadFile(file, "Failed to upload file: ");
            Carousel carousel = carouselMapper.fromDTO(
                    new CarouselDTO(null, fileUrl, determineFileType(file), null, null)
            );

            carouselRepo.save(carousel);
            advancedStructuredLogger.logBuilder()
                    .withEventType("CAROUSEL_ADD_SUCCESS")
                    .withMessage("Carousel item added successfully")
                    .withField("carouselId", carousel.getId())
                    .withField("fileUrl", fileUrl)
                    .withLevel("INFO")
                    .log();
        } catch (Exception e) {
            advancedStructuredLogger.logBuilder()
                    .withEventType("CAROUSEL_ADD_EXCEPTION")
                    .withMessage("Error occurred while adding carousel item")
                    .withField("error", e.getMessage())
                    .withLevel("ERROR")
                    .log();
            throw new AppException("An error occurred while adding the carousel item.", e);
        }
    }

    @Override
    public void updateCarouselItem(String code, CarouselDTO carouselDTO, MultipartFile file) {
        advancedStructuredLogger.logBuilder()
                .withEventType("CAROUSEL_UPDATE_START")
                .withMessage("Attempting to update carousel item")
                .withField("code", code)
                .withLevel("INFO")
                .log();

        try {
            Carousel carousel = carouselRepo.findByCode(code)
                    .orElseThrow(() -> new NotFoundException("Carousel item with code '" + code + "' not found."));

            if (file != null && !file.isEmpty()) {
                safeDeleteFile(carousel.getFileUrl());
                String fileUrl = uploadFile(file, "Failed to upload new file: ");
                carousel.setFileUrl(fileUrl);
                carousel.setType(determineFileType(file));
                advancedStructuredLogger.logBuilder()
                        .withEventType("CAROUSEL_FILE_UPDATED")
                        .withMessage("File updated for carousel item")
                        .withField("carouselId", carousel.getId())
                        .withField("newFileUrl", fileUrl)
                        .withLevel("INFO")
                        .log();
            }

            carouselRepo.save(carousel);
            advancedStructuredLogger.logBuilder()
                    .withEventType("CAROUSEL_UPDATE_SUCCESS")
                    .withMessage("Carousel item updated successfully")
                    .withField("carouselId", carousel.getId())
                    .withField("code", code)
                    .withLevel("INFO")
                    .log();
        } catch (NotFoundException e) {
            advancedStructuredLogger.logBuilder()
                    .withEventType("CAROUSEL_UPDATE_NOT_FOUND")
                    .withMessage("Carousel item not found for update")
                    .withField("code", code)
                    .withField("error", e.getMessage())
                    .withLevel("ERROR")
                    .log();
            throw e;
        } catch (Exception e) {
            advancedStructuredLogger.logBuilder()
                    .withEventType("CAROUSEL_UPDATE_EXCEPTION")
                    .withMessage("Error occurred while updating carousel item")
                    .withField("code", code)
                    .withField("error", e.getMessage())
                    .withLevel("ERROR")
                    .log();
            throw new AppException("An error occurred while updating the carousel item.", e);
        }
    }

    @Override
    public void deleteCarouselItem(String code) {
        advancedStructuredLogger.logBuilder()
                .withEventType("CAROUSEL_DELETE_START")
                .withMessage("Attempting to delete carousel item")
                .withField("code", code)
                .withLevel("INFO")
                .log();

        try {
            Carousel carousel = carouselRepo.findByCode(code)
                    .orElseThrow(() -> new NotFoundException("Carousel item with code '" + code + "' not found."));

            safeDeleteFile(carousel.getFileUrl());
            carouselRepo.delete(carousel);

            advancedStructuredLogger.logBuilder()
                    .withEventType("CAROUSEL_DELETE_SUCCESS")
                    .withMessage("Carousel item deleted successfully")
                    .withField("code", code)
                    .withLevel("INFO")
                    .log();
        } catch (NotFoundException e) {
            advancedStructuredLogger.logBuilder()
                    .withEventType("CAROUSEL_DELETE_NOT_FOUND")
                    .withMessage("Carousel item not found for deletion")
                    .withField("code", code)
                    .withField("error", e.getMessage())
                    .withLevel("ERROR")
                    .log();
            throw e;
        } catch (Exception e) {
            advancedStructuredLogger.logBuilder()
                    .withEventType("CAROUSEL_DELETE_EXCEPTION")
                    .withMessage("Error occurred while deleting carousel item")
                    .withField("code", code)
                    .withField("error", e.getMessage())
                    .withLevel("ERROR")
                    .log();
            throw new AppException("An error occurred while deleting the carousel item.", e);
        }
    }

    private String uploadFile(MultipartFile file, String errorMessage) {
        advancedStructuredLogger.logBuilder()
                .withEventType("CAROUSEL_FILE_UPLOAD_START")
                .withMessage("Attempting to upload file")
                .withField("fileName", file.getOriginalFilename())
                .withLevel("INFO")
                .log();

        try {
            String fileUrl = cloudAdapter.uploadFile(file);
            advancedStructuredLogger.logBuilder()
                    .withEventType("CAROUSEL_FILE_UPLOAD_SUCCESS")
                    .withMessage("File uploaded successfully")
                    .withField("fileUrl", fileUrl)
                    .withField("fileName", file.getOriginalFilename())
                    .withLevel("INFO")
                    .log();
            return fileUrl;
        } catch (Exception e) {
            advancedStructuredLogger.logBuilder()
                    .withEventType("CAROUSEL_FILE_UPLOAD_ERROR")
                    .withMessage("File upload failed")
                    .withField("fileName", file.getOriginalFilename())
                    .withField("error", e.getMessage())
                    .withLevel("ERROR")
                    .log();
            throw new AppException(errorMessage + e.getMessage());
        }
    }

    private void safeDeleteFile(String fileUrl) {
        if (fileUrl != null && !fileUrl.isBlank()) {
            advancedStructuredLogger.logBuilder()
                    .withEventType("CAROUSEL_FILE_DELETE_START")
                    .withMessage("Attempting to delete file")
                    .withField("fileUrl", fileUrl)
                    .withLevel("INFO")
                    .log();

            try {
                cloudAdapter.deleteFile(fileUrl);
                advancedStructuredLogger.logBuilder()
                        .withEventType("CAROUSEL_FILE_DELETE_SUCCESS")
                        .withMessage("File deleted successfully")
                        .withField("fileUrl", fileUrl)
                        .withLevel("INFO")
                        .log();
            } catch (Exception e) {
                advancedStructuredLogger.logBuilder()
                        .withEventType("CAROUSEL_FILE_DELETE_ERROR")
                        .withMessage("File deletion failed")
                        .withField("fileUrl", fileUrl)
                        .withField("error", e.getMessage())
                        .withLevel("ERROR")
                        .log();
                throw new AppException("Failed to delete associated file: " + e.getMessage(), e);
            }
        }
    }

    private String determineFileType(MultipartFile file) {
        advancedStructuredLogger.logBuilder()
                .withEventType("CAROUSEL_FILE_TYPE_DETECTION")
                .withMessage("Determining file type")
                .withField("fileName", file.getOriginalFilename())
                .withLevel("DEBUG")
                .log();

        try {
            String fileType = Objects.requireNonNull(file.getContentType()).contains("image") ? "image" : "video";
            advancedStructuredLogger.logBuilder()
                    .withEventType("CAROUSEL_FILE_TYPE_DETECTED")
                    .withMessage("File type determined successfully")
                    .withField("fileName", file.getOriginalFilename())
                    .withField("fileType", fileType)
                    .withLevel("INFO")
                    .log();
            return fileType;
        } catch (Exception e) {
            advancedStructuredLogger.logBuilder()
                    .withEventType("CAROUSEL_FILE_TYPE_DETECTION_ERROR")
                    .withMessage("Failed to determine file type")
                    .withField("fileName", file.getOriginalFilename())
                    .withField("error", e.getMessage())
                    .withLevel("ERROR")
                    .log();
            throw new AppException("Unable to determine file type.", e);
        }
    }
}
