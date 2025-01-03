package com.example.tecnosserver.blog.dto;

import java.util.List;

public record BlogDTO(
        String code,
        String title,
        String description,
        String mainPhotoUrl,
        String broschureUrl,
        String viewUrl,
        String seriesCode,

        boolean active,
        List<String> captionCodes
) {}
