package com.example.tecnosserver.webinar.mapper;

import com.example.tecnosserver.webinar.dto.WebinarDTO;
import com.example.tecnosserver.webinar.model.Webinar;
import org.springframework.stereotype.Component;

@Component
public class WebinarMapper {

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
                .imageUrl(webinar.getImageUrl() != null ? webinar.getImageUrl() : null)
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
                .imageUrl(webinarDTO.imageUrl() != null ? webinarDTO.imageUrl() : null)
                .build();
    }
}
