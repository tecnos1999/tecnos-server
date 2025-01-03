package com.example.tecnosserver.caption.dto;

import lombok.AllArgsConstructor;

public record CaptionDTO(
        String code,
        String text,
        String position,
        String photoUrl,
        boolean active
) {}
