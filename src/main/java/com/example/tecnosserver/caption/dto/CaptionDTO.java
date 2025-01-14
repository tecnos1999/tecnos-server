package com.example.tecnosserver.caption.dto;


public record CaptionDTO(
        String code,
        String title,
        String text,
        String position,
        String photoUrl,
        boolean active
) {
}
