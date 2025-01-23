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

        String content,
        String imageUrl,
        String link,

        String documentUrl,
        List<SectionDTO> sections,
        List<PageDTO> subPages,
        List<String> products

) {

}
