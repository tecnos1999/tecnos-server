package com.example.tecnosserver.page.mapper;

import com.example.tecnosserver.page.dto.PageDTO;
import com.example.tecnosserver.page.model.Page;
import com.example.tecnosserver.products.mapper.ProductMapper;
import com.example.tecnosserver.sections.mapper.SectionMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class PageMapper {

    private final SectionMapper sectionMapper;
    private final ProductMapper productMapper;

    public Page fromDTO(PageDTO pageDTO) {
        return Page.builder()
                .id(pageDTO.id())
                .slug(pageDTO.slug())
                .title(pageDTO.title())
                .subtitle(pageDTO.subtitle())
                .imageUrl(pageDTO.imageUrl())
                .link(pageDTO.link())
                .sections(pageDTO.sections() != null ?
                        pageDTO.sections().stream()
                                .map(sectionMapper::fromDTO)
                                .collect(Collectors.toList()) : null)
                .subPages(pageDTO.subPages() != null ?
                        pageDTO.subPages().stream()
                                .map(this::fromDTO)
                                .collect(Collectors.toList()) : null)
                .products(pageDTO.products() != null ?
                        pageDTO.products().stream()
                                .map(productMapper::fromDTO)
                                .collect(Collectors.toList()) : null)
                .build();
    }

    public PageDTO toDTO(Page page) {
        return new PageDTO(
                page.getId(),
                page.getSlug(),
                page.getTitle(),
                page.getSubtitle(),
                page.getImageUrl(),
                page.getLink(),
                page.getSections() != null ?
                        page.getSections().stream()
                                .map(sectionMapper::toDTO)
                                .collect(Collectors.toList()) : null,
                page.getSubPages() != null ?
                        page.getSubPages().stream()
                                .map(this::toDTO)
                                .collect(Collectors.toList()) : null,
                page.getProducts() != null ?
                        page.getProducts().stream()
                                .map(productMapper::mapProductToDTO)
                                .collect(Collectors.toList()) : null
        );
    }
}
