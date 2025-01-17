package com.example.tecnosserver.sections.service;

import com.example.tecnosserver.exceptions.exception.AppException;
import com.example.tecnosserver.exceptions.exception.NotFoundException;
import com.example.tecnosserver.sections.dto.SectionDTO;
import com.example.tecnosserver.sections.mapper.SectionMapper;
import com.example.tecnosserver.sections.model.Section;
import com.example.tecnosserver.sections.repo.SectionRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SectionQueryServiceImpl implements SectionQueryService {

    private final SectionRepo sectionRepo;
    private final SectionMapper sectionMapper;

    @Override
    public Optional<SectionDTO> findSectionById(Long sectionId) {
        if (sectionId == null) {
            throw new AppException("Section ID cannot be null.");
        }

        return sectionRepo.findById(sectionId)
                .map(sectionMapper::toDTO)
                .or(() -> {
                    throw new NotFoundException("Section with ID " + sectionId + " not found.");
                });
    }

    @Override
    public Optional<List<SectionDTO>> findSectionsByPageId(Long pageId) {
        if (pageId == null) {
            throw new AppException("Page ID cannot be null.");
        }

        // Folosim Optional și map pentru a transforma secțiunile în DTO-uri
        return sectionRepo.findByPageId(pageId)
                .map(sections -> {
                    if (sections.isEmpty()) {
                        throw new NotFoundException("No sections found for page ID: " + pageId);
                    }
                    return sections.stream()
                            .map(sectionMapper::toDTO)
                            .collect(Collectors.toList());
                });
    }

    @Override
    public Optional<List<SectionDTO>> findAllSections() {
        List<Section> sections = sectionRepo.findAll();
        if (sections.isEmpty()) {
            throw new NotFoundException("No sections found.");
        }

        List<SectionDTO> sectionDTOs = sections.stream()
                .map(sectionMapper::toDTO)
                .collect(Collectors.toList());

        return Optional.of(sectionDTOs);
    }
}
