package com.example.tecnosserver.events.mapper;


import com.example.tecnosserver.events.dto.EventDTO;
import com.example.tecnosserver.events.model.Event;
import com.example.tecnosserver.image.dto.ImageDTO;
import com.example.tecnosserver.image.mapper.ImageMapper;
import com.example.tecnosserver.image.model.Image;
import org.springframework.stereotype.Component;

@Component
public class EventMapper {

    private final ImageMapper imageMapper;

    public EventMapper(ImageMapper imageMapper) {
        this.imageMapper = imageMapper;
    }

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
                .image(event.getImage() != null ? imageMapper.mapImageToDTO(event.getImage()) : null)
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
                .image(eventDTO.image() != null ? imageMapper.mapDTOToImage(eventDTO.image()) : null)
                .build();
    }


}

