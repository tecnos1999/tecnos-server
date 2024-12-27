package com.example.tecnosserver.webinar.dto;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder(toBuilder = true)
public record WebinarDTO(String webCode,
                         String title,
                         String externalLink,
                         LocalDateTime createdAt,
                         LocalDateTime updatedAt,
                         String imageUrl) {
}
