package com.example.tecnosserver.webinar.service;

import com.example.tecnosserver.exceptions.exception.AlreadyExistsException;
import com.example.tecnosserver.exceptions.exception.AppException;
import com.example.tecnosserver.exceptions.exception.NotFoundException;
import com.example.tecnosserver.image.dto.ImageDTO;
import com.example.tecnosserver.image.mapper.ImageMapper;
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

import java.util.ArrayList;
import java.util.List;

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

        String imageUrl = null;
        if (image != null && !image.isEmpty()) {
            try {
                imageUrl = cloudAdapter.uploadFile(image);
            } catch (Exception e) {
                throw new AppException("Failed to upload image: " + e.getMessage());
            }
        }

        WebinarDTO updatedWebinarDTO = WebinarDTO.builder()
                .webCode(webinarDTO.webCode())
                .title(webinarDTO.title())
                .externalLink(webinarDTO.externalLink())
                .createdAt(webinarDTO.createdAt())
                .updatedAt(webinarDTO.updatedAt())
                .image(imageUrl != null ? new ImageDTO(imageUrl, image.getContentType()) : null)
                .build();

        Webinar webinar = webinarMapper.fromDTO(updatedWebinarDTO);
        webinarRepo.save(webinar);
    }


    @Override
    public void deleteWebinar(String webCode) {
        if (webCode == null || webCode.trim().isEmpty()) {
            throw new AppException("Webinar code cannot be null or empty.");
        }

        Webinar webinar = webinarRepo.findWebinarByWebCode(webCode.trim())
                .orElseThrow(() -> new NotFoundException("Webinar with code '" + webCode + "' not found."));

        if (webinar.getImage() != null && webinar.getImage().getUrl() != null) {
            try {
                cloudAdapter.deleteFile(webinar.getImage().getUrl());
                log.info("Deleted image from cloud: " + webinar.getImage().getUrl());
            } catch (Exception e) {
                throw new AppException("Failed to delete associated image from cloud: " + e.getMessage());
            }
        }

        webinarRepo.delete(webinar);
    }


    @Override
    public void updateWebinar(WebinarDTO webinarDTO, MultipartFile image) {
        validateWebinarDTO(webinarDTO);

        Webinar webinar = webinarRepo.findWebinarByWebCode(webinarDTO.webCode())
                .orElseThrow(() -> new NotFoundException("Webinar with code '" + webinarDTO.webCode() + "' not found."));

        if (image != null && !image.isEmpty()) {
            if (webinar.getImage() != null && webinar.getImage().getUrl() != null) {
                try {
                    cloudAdapter.deleteFile(webinar.getImage().getUrl());
                    log.info("Deleted old image from cloud: " + webinar.getImage().getUrl());
                } catch (Exception e) {
                    throw new AppException("Failed to delete old associated image from cloud: " + e.getMessage());
                }
            }

            String imageUrl = null;
            try {
                imageUrl = cloudAdapter.uploadFile(image);
            } catch (Exception e) {
                throw new AppException("Failed to upload new image: " + e.getMessage());
            }

            webinar.setImage(ImageMapper.mapDTOToImage(new ImageDTO(imageUrl, image.getContentType())));
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
}
