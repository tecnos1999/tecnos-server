package com.example.tecnosserver.events.service;


import com.example.tecnosserver.events.dto.EventDTO;
import com.example.tecnosserver.events.mapper.EventMapper;
import com.example.tecnosserver.events.model.Event;
import com.example.tecnosserver.events.repo.EventRepo;
import com.example.tecnosserver.exceptions.exception.AppException;
import com.example.tecnosserver.exceptions.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EventQueryServiceImpl implements EventQueryService {

    private final EventRepo eventRepo;
    private final EventMapper eventMapper;

    @Override
    public Optional<EventDTO> findEventByCode(String eventCode) {
        if (eventCode == null || eventCode.trim().isEmpty()) {
            throw new AppException("Event code cannot be null or empty.");
        }

        return eventRepo.findEventByEventCode(eventCode)
                .map(eventMapper::toDTO);
    }

    @Override
    public Optional<List<EventDTO>> findAllEvents() {
        List<Event> events = eventRepo.findAll();
        if (events.isEmpty()) {
            throw new NotFoundException("No events found.");
        }

        List<EventDTO> eventDTOs = events.stream()
                .map(eventMapper::toDTO)
                .collect(Collectors.toList());

        return Optional.of(eventDTOs);
    }
}

