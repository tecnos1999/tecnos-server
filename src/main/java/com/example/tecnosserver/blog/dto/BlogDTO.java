package com.example.tecnosserver.blog.dto;

import com.example.tecnosserver.caption.dto.CaptionDTO;

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
        List<CaptionDTO> captions
) {}
