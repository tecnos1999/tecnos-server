package com.example.tecnosserver.infocard.dto;


import java.time.LocalDateTime;
import java.util.List;

public record InfoCardDTO(
        String code,
        String title,
        String description,
        List<String> features,
        boolean active,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}

