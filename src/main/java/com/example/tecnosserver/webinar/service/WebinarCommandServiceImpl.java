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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class WebinarCommandServiceImpl implements WebinarCommandService {

    private final WebinarRepo webinarRepo;
    private final WebinarMapper webinarMapper;
    private final CloudAdapter cloudAdapter;

    @Override
    public void addWebinar(WebinarDTO webinarDTO) {
        validateWebinarDTO(webinarDTO);

        if (webinarRepo.findWebinarByWebCode(webinarDTO.webCode()).isPresent()) {
            throw new AlreadyExistsException("Webinar with code '" + webinarDTO.webCode() + "' already exists.");
        }

        Webinar webinar = webinarMapper.fromDTO(webinarDTO);
        webinarRepo.save(webinar);
    }

    @Override
    public void deleteWebinar(String webCode) {
        if (webCode == null || webCode.trim().isEmpty()) {
            throw new AppException("Webinar code cannot be null or empty.");
        }

        Webinar webinar = webinarRepo.findWebinarByWebCode(webCode.trim())
                .orElseThrow(() -> new NotFoundException("Webinar with code '" + webCode + "' not found."));

        List<String> fileUrls = new ArrayList<>();
        if (webinar.getImage() != null && webinar.getImage().getUrl() != null) {
            fileUrls.add(webinar.getImage().getUrl());
        }

        if (!fileUrls.isEmpty()) {
            try {
                cloudAdapter.deleteFiles(fileUrls);
            } catch (Exception e) {
                throw new AppException("Failed to delete associated files from cloud: " + e.getMessage());
            }
        }

        webinarRepo.delete(webinar);
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
