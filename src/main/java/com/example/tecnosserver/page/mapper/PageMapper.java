package com.example.tecnosserver.page.mapper;

import com.example.tecnosserver.page.dto.CreatePageDTO;
import com.example.tecnosserver.page.model.Page;
import com.example.tecnosserver.sections.dto.CreateSectionDTO;
import com.example.tecnosserver.sections.model.Section;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
public class PageMapper {

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
}
