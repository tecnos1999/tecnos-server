package com.example.tecnosserver.partners.dto;

import com.example.tecnosserver.image.dto.ImageDTO;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record PartnerDTO(
        String name,
        String description,
        String catalogFile,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        ImageDTO image
) {
}
