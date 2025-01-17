package com.example.tecnosserver.sections.service;

import com.example.tecnosserver.sections.dto.SectionDTO;
import org.springframework.web.multipart.MultipartFile;

public interface SectionCommandService {
    void addSection(SectionDTO sectionDTO, MultipartFile image);
    void updateSection(Long sectionId, SectionDTO sectionDTO, MultipartFile image);
    void deleteSection(Long sectionId);
}
