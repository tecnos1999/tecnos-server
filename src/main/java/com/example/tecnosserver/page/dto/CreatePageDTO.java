package com.example.tecnosserver.page.dto;

import com.example.tecnosserver.sections.dto.CreateSectionDTO;

import java.util.List;

public record CreatePageDTO(
        String slug,
        String title,
        String subtitle,
        String link,
        List<String> products,
        List<String> subPages,
        List<CreateSectionDTO> sections
) {}
