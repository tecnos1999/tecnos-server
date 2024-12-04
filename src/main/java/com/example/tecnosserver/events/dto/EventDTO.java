package com.example.tecnosserver.events.dto;


import com.example.tecnosserver.image.dto.ImageDTO;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record EventDTO(
        String eventCode,
        String title,
        String description,
        String externalLink,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        ImageDTO image
) {}

