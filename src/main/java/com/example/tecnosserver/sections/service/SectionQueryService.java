package com.example.tecnosserver.sections.service;
import com.example.tecnosserver.sections.dto.SectionDTO;

import java.util.List;
import java.util.Optional;

public interface SectionQueryService {
    Optional<SectionDTO> findSectionById(Long sectionId);

    Optional<List<SectionDTO>> findSectionsByPageId(Long pageId);

    Optional<List<SectionDTO>> findAllSections();
}

