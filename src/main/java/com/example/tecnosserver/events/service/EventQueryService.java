package com.example.tecnosserver.events.service;
import com.example.tecnosserver.events.dto.EventDTO;

import java.util.List;
import java.util.Optional;

public interface EventQueryService {
    Optional<EventDTO> findEventByCode(String eventCode);
    Optional<List<EventDTO>> findAllEvents();
}

