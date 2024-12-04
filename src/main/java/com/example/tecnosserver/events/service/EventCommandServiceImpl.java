package com.example.tecnosserver.events.service;


import com.example.tecnosserver.events.dto.EventDTO;
import com.example.tecnosserver.events.mapper.EventMapper;
import com.example.tecnosserver.events.model.Event;
import com.example.tecnosserver.events.repo.EventRepo;
import com.example.tecnosserver.exceptions.exception.AlreadyExistsException;
import com.example.tecnosserver.exceptions.exception.AppException;
import com.example.tecnosserver.exceptions.exception.NotFoundException;
import com.example.tecnosserver.intercom.CloudAdapter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class EventCommandServiceImpl implements EventCommandService {

    private final EventRepo eventRepo;
    private final EventMapper eventMapper;
    private final CloudAdapter cloudAdapter;

    @Override
    public void addEvent(EventDTO eventDTO) {
        validateEventDTO(eventDTO);

        if (eventRepo.findEventByEventCode(eventDTO.eventCode()).isPresent()) {
            throw new AlreadyExistsException("Event with code '" + eventDTO.eventCode() + "' already exists.");
        }

        Event event = eventMapper.fromDTO(eventDTO);
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
