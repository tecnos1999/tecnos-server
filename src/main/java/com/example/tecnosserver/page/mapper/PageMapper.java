package com.example.tecnosserver.page.mapper;

import com.example.tecnosserver.page.dto.CreatePageDTO;
import com.example.tecnosserver.page.dto.PageDTO;
import com.example.tecnosserver.page.model.Page;
import com.example.tecnosserver.products.model.Product;
import com.example.tecnosserver.sections.dto.CreateSectionDTO;
import com.example.tecnosserver.sections.dto.SectionDTO;
import com.example.tecnosserver.sections.model.Section;
import org.springframework.stereotype.Component;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.stream.Collectors;

@Component
public class PageMapper {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ISO_DATE_TIME;

    public Page fromCreateDTO(CreatePageDTO dto) {
        return Page.builder()
                .slug(dto.slug())
                .title(dto.title())
                .subtitle(dto.subtitle())
                .link(dto.link())
                .sections(new ArrayList<>())
                .subPages(new ArrayList<>())
                .products(new ArrayList<>())
                .build();
    }

    public Section createSection(CreateSectionDTO dto, Page page) {
        return Section.builder()
                .title(dto.title())
                .content(dto.content())
                .position(dto.position())
                .page(page)
                .build();
    }

    public PageDTO toDTO(Page page) {
        return PageDTO.builder()
                .slug(page.getSlug())
                .title(page.getTitle())
                .subtitle(page.getSubtitle())
                .imageUrl(page.getImageUrl())
                .link(page.getLink())
                .sections(page.getSections() != null
                        ? page.getSections().stream()
                        .map(this::toSectionDTO)
                        .collect(Collectors.toList())
                        : new ArrayList<>())
                .subPages(page.getSubPages() != null
                        ? page.getSubPages().stream()
                        .map(Page::getSlug)
                        .collect(Collectors.toList())
                        : new ArrayList<>())
                .products(page.getProducts() != null
                        ? page.getProducts().stream()
                        .map(Product::getSku)
                        .collect(Collectors.toList())
                        : new ArrayList<>())
                .build();
    }

    private SectionDTO toSectionDTO(Section section) {
        return SectionDTO.builder()
                .title(section.getTitle())
                .content(section.getContent())
                .position(section.getPosition())
                .imageUrl(section.getImageUrl())
                .build();
    }
}
