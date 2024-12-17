package com.example.tecnosserver.events.service;


import com.example.tecnosserver.events.dto.EventDTO;
import com.example.tecnosserver.events.mapper.EventMapper;
import com.example.tecnosserver.events.model.Event;
import com.example.tecnosserver.events.repo.EventRepo;
import com.example.tecnosserver.exceptions.exception.AlreadyExistsException;
import com.example.tecnosserver.exceptions.exception.AppException;
import com.example.tecnosserver.exceptions.exception.NotFoundException;
import com.example.tecnosserver.image.dto.ImageDTO;
import com.example.tecnosserver.image.mapper.ImageMapper;
import com.example.tecnosserver.intercom.CloudAdapter;
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
public class EventCommandServiceImpl implements EventCommandService {

    private final EventRepo eventRepo;
    private final EventMapper eventMapper;
    private final CloudAdapter cloudAdapter;

    @Override
    public void addEvent(EventDTO eventDTO, MultipartFile image) {
        validateEventDTO(eventDTO);

        if (eventRepo.findEventByEventCode(eventDTO.eventCode()).isPresent()) {
            throw new AlreadyExistsException("Event with code '" + eventDTO.eventCode() + "' already exists.");
        }

        Event event = eventMapper.fromDTO(eventDTO);

        if (image != null && !image.isEmpty()) {
            try {
                String imageUrl = cloudAdapter.uploadFile(image);
                event.setImage(ImageMapper.mapDTOToImage(new ImageDTO(imageUrl, image.getContentType())));
            } catch (Exception e) {
                throw new AppException("Failed to upload image: " + e.getMessage());
            }
        }

        eventRepo.save(event);
    }


    @Override
    public void updateEvent(EventDTO eventDTO, MultipartFile image) {
        validateEventDTO(eventDTO);

        Event event = eventRepo.findEventByEventCode(eventDTO.eventCode())
                .orElseThrow(() -> new NotFoundException("Event with code '" + eventDTO.eventCode() + "' not found."));

        if (image != null && !image.isEmpty()) {
            if (event.getImage() != null && event.getImage().getUrl() != null) {
                try {
                    cloudAdapter.deleteFile(event.getImage().getUrl());
                    log.info("Deleted old image from cloud: " + event.getImage().getUrl());
                } catch (Exception e) {
                    throw new AppException("Failed to delete old associated image from cloud: " + e.getMessage());
                }

                try {
                    String imageUrl = cloudAdapter.uploadFile(image);
                    event.getImage().setUrl(imageUrl);
                    event.getImage().setType(image.getContentType());
                } catch (Exception e) {
                    throw new AppException("Failed to upload new image: " + e.getMessage());
                }
            } else {
                try {
                    String imageUrl = cloudAdapter.uploadFile(image);
                    event.setImage(ImageMapper.mapDTOToImage(new ImageDTO(imageUrl, image.getContentType())));
                } catch (Exception e) {
                    throw new AppException("Failed to upload new image: " + e.getMessage());
                }
            }
        }

        event.setTitle(eventDTO.title());
        event.setDescription(eventDTO.description());
        event.setExternalLink(eventDTO.externalLink());
        event.setUpdatedAt(eventDTO.updatedAt());

        eventRepo.save(event);
    }


    @Override
    public void deleteEvent(String eventCode) {
        if (eventCode == null || eventCode.trim().isEmpty()) {
            throw new AppException("Event code cannot be null or empty.");
        }

        Event event = eventRepo.findEventByEventCode(eventCode.trim())
                .orElseThrow(() -> new NotFoundException("Event with code '" + eventCode + "' not found."));

        List<String> fileUrls = new ArrayList<>();
        if (event.getImage() != null && event.getImage().getUrl() != null) {
            fileUrls.add(event.getImage().getUrl());
        }

        if (!fileUrls.isEmpty()) {
            try {
                cloudAdapter.deleteFiles(fileUrls);
            } catch (Exception e) {
                throw new AppException("Failed to delete associated files from cloud: " + e.getMessage());
            }
        }

        eventRepo.delete(event);
    }

    private void validateEventDTO(EventDTO eventDTO) {
        if (eventDTO == null) {
            throw new AppException("Event data cannot be null.");
        }
        if (eventDTO.title() == null || eventDTO.title().trim().isEmpty()) {
            throw new AppException("Event title cannot be null or empty.");
        }
        if (eventDTO.description() == null || eventDTO.description().trim().isEmpty()) {
            throw new AppException("Description cannot be null or empty.");
        }
        if (eventDTO.externalLink() == null || eventDTO.externalLink().trim().isEmpty()) {
            throw new AppException("External link cannot be null or empty.");
        }
    }
}
