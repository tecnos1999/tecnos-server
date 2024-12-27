package com.example.tecnosserver.events.mapper;

import com.example.tecnosserver.events.dto.EventDTO;
import com.example.tecnosserver.events.model.Event;
import org.springframework.stereotype.Component;

@Component
public class EventMapper {

    public EventDTO toDTO(Event event) {
        if (event == null) {
            return null;
        }

        return EventDTO.builder()
                .eventCode(event.getEventCode())
                .title(event.getTitle())
                .description(event.getDescription())
                .externalLink(event.getExternalLink())
                .createdAt(event.getCreatedAt())
                .updatedAt(event.getUpdatedAt())
                .imageUrl(event.getImageUrl())
                .build();
    }

    public Event fromDTO(EventDTO eventDTO) {
        if (eventDTO == null) {
            return null;
        }

        return Event.builder()
                .eventCode(eventDTO.eventCode())
                .title(eventDTO.title())
                .description(eventDTO.description())
                .externalLink(eventDTO.externalLink())
                .imageUrl(eventDTO.imageUrl())
                .build();
    }
}
