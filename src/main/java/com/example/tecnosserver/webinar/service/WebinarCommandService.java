package com.example.tecnosserver.webinar.service;

import com.example.tecnosserver.events.dto.EventDTO;
import com.example.tecnosserver.webinar.dto.WebinarDTO;
import org.springframework.web.multipart.MultipartFile;

public interface WebinarCommandService {

    void addWebinar(WebinarDTO webinarDTO, MultipartFile image);
    void deleteWebinar(String webCode);

    void updateWebinar(WebinarDTO webinarDTO, MultipartFile image);
}
