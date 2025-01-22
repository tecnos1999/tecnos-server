package com.example.tecnosserver.page.dto;

import com.example.tecnosserver.sections.dto.SectionDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.util.List;


@Builder
public record PageDTO(
        String slug,
        String title,
        String subtitle,
        String imageUrl,
        String link,
        List<SectionDTO> sections,
        List<String> subPages,
        List<String> products,
        String createdAt,
        String updatedAt
) {

}
