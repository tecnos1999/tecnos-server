package com.example.tecnosserver.events.service;


import com.example.tecnosserver.events.dto.EventDTO;
import org.springframework.web.multipart.MultipartFile;

public interface EventCommandService {
    void addEvent(EventDTO eventDTO, MultipartFile image);

    void deleteEvent(String eventCode);

    void updateEvent(EventDTO eventDTO, MultipartFile image);

}

