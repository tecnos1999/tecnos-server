package com.example.tecnosserver.webinar.service;

import com.example.tecnosserver.exceptions.exception.AlreadyExistsException;
import com.example.tecnosserver.exceptions.exception.AppException;
import com.example.tecnosserver.exceptions.exception.NotFoundException;
import com.example.tecnosserver.intercom.CloudAdapter;
import com.example.tecnosserver.webinar.dto.WebinarDTO;
import com.example.tecnosserver.webinar.mapper.WebinarMapper;
import com.example.tecnosserver.webinar.model.Webinar;
import com.example.tecnosserver.webinar.repo.WebinarRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class WebinarCommandServiceImpl implements WebinarCommandService {

    private final WebinarRepo webinarRepo;
    private final WebinarMapper webinarMapper;
    private final CloudAdapter cloudAdapter;

    @Override
    public void addWebinar(WebinarDTO webinarDTO, MultipartFile image) {
        validateWebinarDTO(webinarDTO);

        if (webinarRepo.findWebinarByWebCode(webinarDTO.webCode()).isPresent()) {
            throw new AlreadyExistsException("Webinar with code '" + webinarDTO.webCode() + "' already exists.");
        }

        String imageUrl = uploadFile(image, "Failed to upload image: ");
        Webinar webinar = webinarMapper.fromDTO(webinarDTO);

        if (imageUrl != null) {
            webinar.setImageUrl(imageUrl);
        }

        webinarRepo.save(webinar);
    }

    @Override
    public void deleteWebinar(String webCode) {
        if (webCode == null || webCode.trim().isEmpty()) {
            throw new AppException("Webinar code cannot be null or empty.");
        }

        Webinar webinar = webinarRepo.findWebinarByWebCode(webCode.trim())
                .orElseThrow(() -> new NotFoundException("Webinar with code '" + webCode + "' not found."));

        safeDeleteFile(webinar.getImageUrl(), "Failed to delete associated image from cloud: ");

        webinarRepo.delete(webinar);
    }

    @Override
    public void updateWebinar(WebinarDTO webinarDTO, MultipartFile image) {
        validateWebinarDTO(webinarDTO);

        Webinar webinar = webinarRepo.findWebinarByWebCode(webinarDTO.webCode())
                .orElseThrow(() -> new NotFoundException("Webinar with code '" + webinarDTO.webCode() + "' not found."));

        if (image != null && !image.isEmpty()) {
            safeDeleteFile(webinar.getImageUrl(), "Failed to delete old image from cloud: ");
            String imageUrl = uploadFile(image, "Failed to upload new image: ");
            webinar.setImageUrl(imageUrl);
        }

        webinar.setTitle(webinarDTO.title());
        webinar.setExternalLink(webinarDTO.externalLink());
        webinar.setUpdatedAt(webinarDTO.updatedAt());

        webinarRepo.save(webinar);
    }

    private void validateWebinarDTO(WebinarDTO webinarDTO) {
        if (webinarDTO == null) {
            throw new AppException("Webinar data cannot be null.");
        }
        if (webinarDTO.title() == null || webinarDTO.title().trim().isEmpty()) {
            throw new AppException("Webinar title cannot be null or empty.");
        }
        if (webinarDTO.externalLink() == null || webinarDTO.externalLink().trim().isEmpty()) {
            throw new AppException("External link cannot be null or empty.");
        }
    }

    private String uploadFile(MultipartFile file, String errorMessage) {
        if (file != null && !file.isEmpty()) {
            try {
                return cloudAdapter.uploadFile(file);
            } catch (Exception e) {
                throw new AppException(errorMessage + e.getMessage());
            }
        }
        return null;
    }

    private void safeDeleteFile(String fileUrl, String errorMessage) {
        if (fileUrl != null && !fileUrl.isBlank()) {
            try {
                cloudAdapter.deleteFile(fileUrl);
                log.info("Deleted file from cloud: " + fileUrl);
            } catch (Exception e) {
                log.error(errorMessage + e.getMessage());
            }
        }
    }
}
