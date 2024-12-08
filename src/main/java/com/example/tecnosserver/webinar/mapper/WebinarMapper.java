package com.example.tecnosserver.webinar.mapper;

import com.example.tecnosserver.events.dto.EventDTO;
import com.example.tecnosserver.events.model.Event;
import com.example.tecnosserver.image.dto.ImageDTO;
import com.example.tecnosserver.image.mapper.ImageMapper;
import com.example.tecnosserver.image.model.Image;
import com.example.tecnosserver.webinar.dto.WebinarDTO;
import com.example.tecnosserver.webinar.model.Webinar;
import org.springframework.stereotype.Component;

@Component
public class WebinarMapper {
    private final ImageMapper imageMapper;

    public WebinarMapper(ImageMapper imageMapper) {
        this.imageMapper = imageMapper;
    }
    public WebinarDTO toDTO(Webinar webinar) {
        if (webinar == null) {
            return null;
        }

        return WebinarDTO.builder()
                .webCode(webinar.getWebCode())
                .title(webinar.getTitle())
                .externalLink(webinar.getExternalLink())
                .createdAt(webinar.getCreatedAt())
                .updatedAt(webinar.getUpdatedAt())
                .image(webinar.getImage() != null ? imageMapper.mapImageToDTO(webinar.getImage()) : null)
                .build();
    }

    public Webinar fromDTO(WebinarDTO webinarDTO) {
        if (webinarDTO == null) {
            return null;
        }

        return Webinar.builder()
                .webCode(webinarDTO.webCode())
                .title(webinarDTO.title())
                .externalLink(webinarDTO.externalLink())
                .image(webinarDTO.image() != null ? imageMapper.mapDTOToImage(webinarDTO.image()) : null)
                .build();
    }


}

