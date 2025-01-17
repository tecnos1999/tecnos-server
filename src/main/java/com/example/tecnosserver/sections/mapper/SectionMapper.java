package com.example.tecnosserver.sections.mapper;

import com.example.tecnosserver.sections.dto.SectionDTO;
import com.example.tecnosserver.sections.model.Section;
import org.springframework.stereotype.Component;

@Component
public class SectionMapper {
    public Section fromDTO(SectionDTO sectionDTO) {
        return Section.builder()
                .id(sectionDTO.id())
                .title(sectionDTO.title())
                .content(sectionDTO.content())
                .imageUrl(sectionDTO.imageUrl())
                .build();
    }

    public SectionDTO toDTO(Section section) {
        return new SectionDTO(
                section.getId(),
                section.getTitle(),
                section.getContent(),
                section.getImageUrl(),
                section.getPage().getId()
        );
    }
}
