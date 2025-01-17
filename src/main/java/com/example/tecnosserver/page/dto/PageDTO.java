package com.example.tecnosserver.page.dto;

import com.example.tecnosserver.sections.dto.SectionDTO;
import com.example.tecnosserver.products.dto.ProductDTO;

import java.util.List;

public record PageDTO(
        Long id,
        String slug,
        String title,
        String subtitle,
        String imageUrl,
        String link,
        List<SectionDTO> sections,
        List<PageDTO> subPages,
        List<ProductDTO> products
) {}
