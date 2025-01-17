package com.example.tecnosserver.sections.service;

import com.example.tecnosserver.exceptions.exception.AppException;
import com.example.tecnosserver.exceptions.exception.NotFoundException;
import com.example.tecnosserver.intercom.CloudAdapter;
import com.example.tecnosserver.page.repo.PageRepo;
import com.example.tecnosserver.sections.dto.SectionDTO;
import com.example.tecnosserver.sections.mapper.SectionMapper;
import com.example.tecnosserver.sections.model.Section;
import com.example.tecnosserver.sections.repo.SectionRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class SectionCommandServiceImpl implements SectionCommandService {

    private final SectionRepo sectionRepo;
    private final PageRepo pageRepository;
    private final SectionMapper sectionMapper;
    private final CloudAdapter cloudAdapter;

    @Override
    public void addSection(SectionDTO sectionDTO, MultipartFile image) {
        validateSectionDTO(sectionDTO);

        String imageUrl = uploadFile(image, "Failed to upload image: ");

        Section section = sectionMapper.fromDTO(sectionDTO);
        section.setImageUrl(imageUrl);

        section.setPage(pageRepository.findById(sectionDTO.pageId())
                .orElseThrow(() -> new NotFoundException("Page not found with ID: " + sectionDTO.pageId())));

        sectionRepo.save(section);
    }

    @Override
    public void updateSection(Long sectionId, SectionDTO sectionDTO, MultipartFile image) {
        validateSectionDTO(sectionDTO);

        Section section = sectionRepo.findById(sectionId)
                .orElseThrow(() -> new NotFoundException("Section not found with ID: " + sectionId));

        if (image != null && !image.isEmpty()) {
            safeDeleteFile(section.getImageUrl(), "Failed to delete old image: ");
            String imageUrl = uploadFile(image, "Failed to upload new image: ");
            section.setImageUrl(imageUrl);
        }

        section.setTitle(sectionDTO.title());
        section.setContent(sectionDTO.content());

        sectionRepo.save(section);
    }

    @Override
    public void deleteSection(Long sectionId) {
        Section section = sectionRepo.findById(sectionId)
                .orElseThrow(() -> new NotFoundException("Section not found with ID: " + sectionId));

        safeDeleteFile(section.getImageUrl(), "Failed to delete associated image: ");

        sectionRepo.delete(section);
    }

    private void validateSectionDTO(SectionDTO sectionDTO) {
        if (sectionDTO == null) {
            throw new AppException("Section data cannot be null.");
        }
        if (sectionDTO.pageId() == null) {
            throw new AppException("Page ID cannot be null.");
        }
    }

    private String uploadFile(MultipartFile file, String errorMessage) {
        if (file != null && !file.isEmpty()) {
            try {
                return cloudAdapter.uploadFile(file);
            } catch (Exception e) {
                throw new AppException(errorMessage + e.getMessage());
            }
        }
        return null;
    }

    private void safeDeleteFile(String fileUrl, String errorMessage) {
        if (fileUrl != null && !fileUrl.isBlank()) {
            try {
                cloudAdapter.deleteFile(fileUrl);
                log.info("Deleted file from cloud: " + fileUrl);
            } catch (Exception e) {
                log.error(errorMessage + e.getMessage());
            }
        }
    }
}
