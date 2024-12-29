package com.example.tecnosserver.carousel.dto;

import java.time.LocalDateTime;


public record CarouselDTO(
        String code,
        String fileUrl,
        String type,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {}



