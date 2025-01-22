package com.example.tecnosserver.page.service;

import com.example.tecnosserver.exceptions.exception.AlreadyExistsException;
import com.example.tecnosserver.exceptions.exception.AppException;
import com.example.tecnosserver.exceptions.exception.NotFoundException;
import com.example.tecnosserver.page.dto.CreatePageDTO;
import com.example.tecnosserver.page.dto.PageResponseDTO;
import com.example.tecnosserver.page.mapper.PageMapper;
import com.example.tecnosserver.page.model.Page;
import com.example.tecnosserver.page.repo.PageRepo;
import com.example.tecnosserver.products.model.Product;
import com.example.tecnosserver.products.repo.ProductRepo;
import com.example.tecnosserver.sections.dto.CreateSectionDTO;
import com.example.tecnosserver.sections.model.Section;
import com.example.tecnosserver.sections.repo.SectionRepo;
import com.example.tecnosserver.intercom.CloudAdapter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class PageCommandServiceImpl implements PageCommandService {

    private final PageRepo pageRepo;
    private final ProductRepo productRepo;
    private final SectionRepo sectionRepo;
    private final PageMapper pageMapper;
    private final CloudAdapter cloudAdapter;

    @Override
    public PageResponseDTO createPage(CreatePageDTO createPageDTO, MultipartFile pageImage, Map<Integer, MultipartFile> sectionImages) {
        if (pageRepo.existsBySlug(createPageDTO.slug())) {
            throw new AlreadyExistsException("Page with slug '" + createPageDTO.slug() + "' already exists.");
        }

        Page page = pageMapper.fromCreateDTO(createPageDTO);

        if (pageImage != null && !pageImage.isEmpty()) {
            String pageImageUrl = cloudAdapter.uploadFile(pageImage);
            page.setImageUrl(pageImageUrl);
        }

        if (createPageDTO.products() != null && !createPageDTO.products().isEmpty()) {
            List<Product> products = productRepo.findBySkuIn(createPageDTO.products());
            products.forEach(page::addProduct);
        }

        if (createPageDTO.subPages() != null && !createPageDTO.subPages().isEmpty()) {
            List<Page> subPages = pageRepo.findBySlugIn(createPageDTO.subPages()).get();
            if (subPages.isEmpty()) {
                throw new NotFoundException("Sub-pages not found.");
            }
            page.setSubPages(subPages);
        }

        page = pageRepo.save(page);

        if (createPageDTO.sections() != null) {
            List<Section> sections = new ArrayList<>();

            for (int i = 0; i < createPageDTO.sections().size(); i++) {
                CreateSectionDTO sectionDTO = createPageDTO.sections().get(i);
                Section section = pageMapper.createSection(sectionDTO, page);

                MultipartFile sectionImage = sectionImages.get(i);
                if (sectionImage != null && !sectionImage.isEmpty()) {
                    String sectionImageUrl = cloudAdapter.uploadFile(sectionImage);
                    section.setImageUrl(sectionImageUrl);
                }

                sections.add(section);
            }

            sectionRepo.saveAll(sections);
            page.setSections(sections);
        }

        page = pageRepo.save(page);

        return new PageResponseDTO(
                page.getId(),
                page.getSlug(),
                "Page created successfully with " +
                        (page.getSections() != null ? page.getSections().size() : 0) +
                        " sections"
        );
    }

    @Override
    public void updatePage(String slug, CreatePageDTO createPageDTO, MultipartFile pageImage, Map<Integer, MultipartFile> sectionImages) {
        // 1. Find existing page
        Page existingPage = pageRepo.findBySlug(slug)
                .orElseThrow(() -> new NotFoundException("Page with slug '" + slug + "' not found."));

        // 2. Update basic fields
        existingPage.setTitle(createPageDTO.title());
        existingPage.setSubtitle(createPageDTO.subtitle());
        existingPage.setLink(createPageDTO.link());

        // 3. Handle page image update
        if (pageImage != null && !pageImage.isEmpty()) {
            if (existingPage.getImageUrl() != null) {
                cloudAdapter.deleteFile(existingPage.getImageUrl());
            }
            String newImageUrl = uploadFile(pageImage);
            existingPage.setImageUrl(newImageUrl);
        }

        // 4. Handle products update
        if (createPageDTO.products() != null) {
            Set<String> newProductSkus = new HashSet<>(createPageDTO.products());
            Set<String> existingProductSkus = existingPage.getProducts().stream()
                    .map(Product::getSku)
                    .collect(Collectors.toSet());

            // Remove products that are no longer in the DTO
            existingPage.getProducts().removeIf(product -> !newProductSkus.contains(product.getSku()));

            // Add new products
            Set<String> skusToAdd = newProductSkus.stream()
                    .filter(sku -> !existingProductSkus.contains(sku))
                    .collect(Collectors.toSet());

            if (!skusToAdd.isEmpty()) {
                List<Product> newProducts = productRepo.findBySkuIn(new ArrayList<>(skusToAdd));
                newProducts.forEach(existingPage::addProduct);
            }
        }

        // 5. Handle subpages update
        if (createPageDTO.subPages() != null) {
            Set<String> newPageSlugs = new HashSet<>(createPageDTO.subPages());
            Set<String> existingPageSlugs = existingPage.getSubPages().stream()
                    .map(Page::getSlug)
                    .collect(Collectors.toSet());

            // Remove subpages that are no longer in the DTO
            existingPage.getSubPages().removeIf(page -> !newPageSlugs.contains(page.getSlug()));

            // Add new subpages
            Set<String> newSlugsToAdd = newPageSlugs.stream()
                    .filter(pageSlug -> !existingPageSlugs.contains(pageSlug))
                    .collect(Collectors.toSet());

            if (!newSlugsToAdd.isEmpty()) {
                Optional<List<Page>> newSubPages = pageRepo.findBySlugIn(new ArrayList<>(newSlugsToAdd));
                newSubPages.ifPresent(pages -> pages.forEach(existingPage::addSubPage));
            }
        }

        // Save the page to ensure all basic updates are persisted
        Page savedPage = pageRepo.save(existingPage);

        // 6. Handle sections update
        if (createPageDTO.sections() != null) {
            // Get existing sections
            List<Section> existingSections = sectionRepo.findByPage(savedPage);

            // Create maps for easier comparison
            Map<String, Section> existingSectionMap = existingSections.stream()
                    .collect(Collectors.toMap(
                            section -> section.getTitle() + "-" + section.getPosition(),
                            section -> section
                    ));

            Map<String, CreateSectionDTO> newSectionMap = createPageDTO.sections().stream()
                    .collect(Collectors.toMap(
                            section -> section.title() + "-" + section.position(),
                            section -> section
                    ));

            // Track sections to update, create, and delete
            List<Section> sectionsToUpdate = new ArrayList<>();
            List<Section> sectionsToDelete = new ArrayList<>();

            // Find sections to update or delete
            for (Section existingSection : existingSections) {
                String key = existingSection.getTitle() + "-" + existingSection.getPosition();
                if (newSectionMap.containsKey(key)) {
                    // Update existing section
                    CreateSectionDTO sectionDTO = newSectionMap.get(key);
                    existingSection.setContent(sectionDTO.content());
                    sectionsToUpdate.add(existingSection);
                } else {
                    // Section no longer exists in DTO
                    sectionsToDelete.add(existingSection);
                }
            }

            // Delete removed sections and their images
            for (Section section : sectionsToDelete) {
                if (section.getImageUrl() != null) {
                    cloudAdapter.deleteFile(section.getImageUrl());
                }
                sectionRepo.delete(section);
            }

            // Create new sections
            for (int i = 0; i < createPageDTO.sections().size(); i++) {
                CreateSectionDTO sectionDTO = createPageDTO.sections().get(i);
                String key = sectionDTO.title() + "-" + sectionDTO.position();

                if (!existingSectionMap.containsKey(key)) {
                    // Create new section
                    Section newSection = Section.builder()
                            .title(sectionDTO.title())
                            .content(sectionDTO.content())
                            .position(sectionDTO.position())
                            .page(savedPage)
                            .build();

                    // Handle section image if provided
                    MultipartFile sectionImage = sectionImages.get(i);
                    if (sectionImage != null && !sectionImage.isEmpty()) {
                        String imageUrl = uploadFile(sectionImage);
                        newSection.setImageUrl(imageUrl);
                    }

                    sectionsToUpdate.add(newSection);
                } else {
                    // Update image for existing section if provided
                    Section existingSection = existingSectionMap.get(key);
                    MultipartFile sectionImage = sectionImages.get(i);
                    if (sectionImage != null && !sectionImage.isEmpty()) {
                        if (existingSection.getImageUrl() != null) {
                            cloudAdapter.deleteFile(existingSection.getImageUrl());
                        }
                        String imageUrl = uploadFile(sectionImage);
                        existingSection.setImageUrl(imageUrl);
                    }
                }
            }

            // Save all updated and new sections
            sectionRepo.saveAll(sectionsToUpdate);
        }
    }

    @Override
    public void deletePage(String slug) {
        Page page = pageRepo.findBySlug(slug)
                .orElseThrow(() -> new NotFoundException("Page with slug '" + slug + "' not found."));

        try {
            if (page.getImageUrl() != null) {
                cloudAdapter.deleteFile(page.getImageUrl());
            }

            if (page.getSections() != null) {
                for (Section section : page.getSections()) {
                    if (section.getImageUrl() != null) {
                        cloudAdapter.deleteFile(section.getImageUrl());
                    }
                }
            }

            page.clearProducts();

            pageRepo.delete(page);

        } catch (Exception e) {
            log.error("Error during page deletion: " + e.getMessage());
            throw new RuntimeException("Failed to delete page: " + e.getMessage());
        }
    }


    private String uploadFile(MultipartFile file) {
        try {
            return cloudAdapter.uploadFile(file);
        } catch (Exception e) {
            log.error("Failed to upload file: {}", e.getMessage());
            throw new AppException(e.getMessage());
        }
    }
}
