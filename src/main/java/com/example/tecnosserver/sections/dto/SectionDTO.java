package com.example.tecnosserver.sections.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;

@Builder
public record SectionDTO(
        String title,
        String content,
        String imageUrl,
        String position
) {
}
