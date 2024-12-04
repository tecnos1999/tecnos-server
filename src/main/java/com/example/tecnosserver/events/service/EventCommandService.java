package com.example.tecnosserver.events.service;


import com.example.tecnosserver.events.dto.EventDTO;

public interface EventCommandService {
    void addEvent(EventDTO eventDTO);
    void deleteEvent(String eventCode);
}

