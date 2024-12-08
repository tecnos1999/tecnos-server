package com.example.tecnosserver.webinar.dto;

import com.example.tecnosserver.image.dto.ImageDTO;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record WebinarDTO(String webCode,
                         String title,
                         String externalLink,
                         LocalDateTime createdAt,
                         LocalDateTime updatedAt,
                         ImageDTO image) {
}
