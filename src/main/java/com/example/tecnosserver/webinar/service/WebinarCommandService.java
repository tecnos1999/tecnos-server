package com.example.tecnosserver.webinar.service;

import com.example.tecnosserver.events.dto.EventDTO;
import com.example.tecnosserver.webinar.dto.WebinarDTO;

public interface WebinarCommandService {

    void addWebinar(WebinarDTO webinarDTO);
    void deleteWebinar(String webCode);
}
