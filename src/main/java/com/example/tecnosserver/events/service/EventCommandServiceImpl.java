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
            String imageUrl = uploadFile(image, "Failed to upload image: ");
            event.setImage(ImageMapper.mapDTOToImage(new ImageDTO(imageUrl, image.getContentType())));
        }

        eventRepo.save(event);
    }

    @Override
    public void updateEvent(EventDTO eventDTO, MultipartFile image) {
        validateEventDTO(eventDTO);

        Event event = eventRepo.findEventByEventCode(eventDTO.eventCode())
                .orElseThrow(() -> new NotFoundException("Event with code '" + eventDTO.eventCode() + "' not found."));

        if (image != null && !image.isEmpty()) {
            if (event.getImage() != null) {
                try {
                    String imageUrl = cloudAdapter.uploadFile(image);
                    event.getImage().setUrl(imageUrl);
                    event.getImage().setType(image.getContentType());
                    log.info("Updated image details in database for event: " + event.getEventCode());
                } catch (Exception e) {
                    throw new AppException("Failed to upload and update image: " + e.getMessage());
                }
            } else {
                String imageUrl = uploadFile(image, "Failed to upload new image: ");
                event.setImage(ImageMapper.mapDTOToImage(new ImageDTO(imageUrl, image.getContentType())));
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

        safeDeleteFile(event.getImage() != null ? event.getImage().getUrl() : null
        );

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

    private void safeDeleteFile(String fileUrl) {
        if (fileUrl != null && !fileUrl.isBlank()) {
            try {
                cloudAdapter.deleteFile(fileUrl);
                log.info("Deleted file from cloud: " + fileUrl);
            } catch (Exception e) {
                log.error("Failed to delete associated image: " + e.getMessage());
            }
        }
    }
}
