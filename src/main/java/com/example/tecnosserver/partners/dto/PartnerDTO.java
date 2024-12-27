package com.example.tecnosserver.partners.dto;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record PartnerDTO(
        String name,
        String description,
        String catalogFile,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        String imageUrl
) {
}
