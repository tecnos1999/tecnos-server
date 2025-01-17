package com.example.tecnosserver.sections.dto;
public record SectionDTO(
        Long id,
        String title,
        String content,
        String imageUrl,
        Long pageId
) {}

