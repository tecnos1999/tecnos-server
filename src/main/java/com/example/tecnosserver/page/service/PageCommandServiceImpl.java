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
    public PageResponseDTO createPage(CreatePageDTO createPageDTO, MultipartFile pageImage,MultipartFile document, Map<String, MultipartFile> sectionImages) {
        if (pageRepo.existsByTitle(createPageDTO.title())) {
            throw new AlreadyExistsException("Page with slug '" + createPageDTO.title() + "' already exists.");
        }

        Page page = pageMapper.fromCreateDTO(createPageDTO);

        if (pageImage != null && !pageImage.isEmpty()) {
            String pageImageUrl = cloudAdapter.uploadFile(pageImage);
            page.setImageUrl(pageImageUrl);
        }

        if (document != null && !document.isEmpty()) {
            String documentUrl = cloudAdapter.uploadFile(document);
            page.setDocumentUrl(documentUrl);
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

            for (CreateSectionDTO sectionDTO : createPageDTO.sections()) {
                Section section = pageMapper.createSection(sectionDTO, page);

                MultipartFile sectionImage = sectionImages.get(sectionDTO.title());
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

    @Transactional
    @Override
    public PageResponseDTO updatePage(
            String slug,
            CreatePageDTO createPageDTO,
            MultipartFile pageImage,
            MultipartFile document,
            Map<String, MultipartFile> sectionImagesMap
    ) {
        Page existingPage = pageRepo.findBySlug(slug)
                .orElseThrow(() -> new NotFoundException("Page with slug '" + slug + "' not found."));

        existingPage.setTitle(createPageDTO.title());
        existingPage.setSlug(createPageDTO.title().toLowerCase().replace(" ", "-"));
        existingPage.setSubtitle(createPageDTO.subtitle());
        existingPage.setLink(createPageDTO.link());
        existingPage.setContent(createPageDTO.content());

        if (pageImage != null && !pageImage.isEmpty()) {
            if (existingPage.getImageUrl() != null) {
                try {
                    cloudAdapter.deleteFile(existingPage.getImageUrl());
                } catch (Exception e) {
                    log.warn("Nu am putut sterge fisierul din cloud: {}", e.getMessage());
                }
            }
            String newImageUrl = uploadFile(pageImage);
            existingPage.setImageUrl(newImageUrl);
        }

        if (document != null && !document.isEmpty()) {
            if (existingPage.getDocumentUrl() != null) {
                try {
                    cloudAdapter.deleteFile(existingPage.getDocumentUrl());
                } catch (Exception e) {
                    log.warn("Nu am putut sterge fisierul din cloud: {}", e.getMessage());
                }
            }
            String newDocumentUrl = uploadFile(document);
            existingPage.setDocumentUrl(newDocumentUrl);
        }

        if (createPageDTO.products() != null) {
            Set<String> newProductSkus = new HashSet<>(createPageDTO.products());
            Set<String> existingProductSkus = existingPage.getProducts().stream()
                    .map(Product::getSku)
                    .collect(Collectors.toSet());

            existingPage.getProducts().removeIf(product -> !newProductSkus.contains(product.getSku()));

            Set<String> skusToAdd = newProductSkus.stream()
                    .filter(sku -> !existingProductSkus.contains(sku))
                    .collect(Collectors.toSet());
            if (!skusToAdd.isEmpty()) {
                List<Product> newProducts = productRepo.findBySkuIn(new ArrayList<>(skusToAdd));
                newProducts.forEach(existingPage::addProduct);
            }
        }

        if (createPageDTO.subPages() != null) {
            Set<String> newPageSlugs = new HashSet<>(createPageDTO.subPages());
            Set<String> existingPageSlugs = existingPage.getSubPages().stream()
                    .map(Page::getSlug)
                    .collect(Collectors.toSet());

            existingPage.getSubPages().removeIf(p -> !newPageSlugs.contains(p.getSlug()));

            Set<String> newSlugsToAdd = newPageSlugs.stream()
                    .filter(s -> !existingPageSlugs.contains(s))
                    .collect(Collectors.toSet());
            if (!newSlugsToAdd.isEmpty()) {
                Optional<List<Page>> newSubPages = pageRepo.findBySlugIn(new ArrayList<>(newSlugsToAdd));
                newSubPages.ifPresent(pages -> pages.forEach(existingPage::addSubPage));
            }
        }

        Page savedPage = pageRepo.save(existingPage);

        if (createPageDTO.sections() != null) {
            List<Section> existingSections = sectionRepo.findByPage(savedPage);

            Map<String, Section> existingSectionMap = existingSections.stream()
                    .collect(Collectors.toMap(Section::getTitle, section -> section));

            Map<String, CreateSectionDTO> newSectionMap = createPageDTO.sections().stream()
                    .collect(Collectors.toMap(CreateSectionDTO::title, dto -> dto));

            List<Section> sectionsToUpdateOrCreate = new ArrayList<>();
            List<Section> sectionsToDelete = new ArrayList<>();

            for (Section existingSection : existingSections) {
                String titleKey = existingSection.getTitle();
                if (newSectionMap.containsKey(titleKey)) {
                    CreateSectionDTO dto = newSectionMap.get(titleKey);
                    existingSection.setContent(dto.content());
                    sectionsToUpdateOrCreate.add(existingSection);
                } else {
                    sectionsToDelete.add(existingSection);
                }
            }

            for (Section sectionToDelete : sectionsToDelete) {
                savedPage.removeSection(sectionToDelete);

                if (sectionToDelete.getImageUrl() != null) {
                    try {
                        cloudAdapter.deleteFile(sectionToDelete.getImageUrl());
                    } catch (Exception e) {
                        log.warn("Could not delete section file from cloud: {}", e.getMessage());
                    }
                }

                sectionToDelete.removePage();

                sectionRepo.delete(sectionToDelete);
            }

            pageRepo.save(savedPage);
            sectionRepo.flush();

            for (CreateSectionDTO dto : createPageDTO.sections()) {
                String titleKey = dto.title();
                if (!existingSectionMap.containsKey(titleKey)) {
                    Section newSection = Section.builder()
                            .title(dto.title())
                            .content(dto.content())
                            .page(savedPage)
                            .build();

                    if (sectionImagesMap.containsKey(titleKey)) {
                        MultipartFile imageFile = sectionImagesMap.get(titleKey);
                        if (imageFile != null && !imageFile.isEmpty()) {
                            String imageUrl = uploadFile(imageFile);
                            newSection.setImageUrl(imageUrl);
                        }
                    }
                    sectionsToUpdateOrCreate.add(newSection);
                } else {
                    Section existingSection = existingSectionMap.get(titleKey);
                    if (sectionImagesMap.containsKey(titleKey)) {
                        MultipartFile imageFile = sectionImagesMap.get(titleKey);
                        if (imageFile != null && !imageFile.isEmpty()) {
                            if (existingSection.getImageUrl() != null) {
                                try {
                                    cloudAdapter.deleteFile(existingSection.getImageUrl());
                                } catch (Exception e) {
                                    log.warn("Could not delete old section file from cloud: {}", e.getMessage());
                                }
                            }
                            String newImageUrl = uploadFile(imageFile);
                            existingSection.setImageUrl(newImageUrl);
                        }
                    }
                }
            }

            sectionRepo.saveAll(sectionsToUpdateOrCreate);
        }

        return new PageResponseDTO(
                savedPage.getId(),
                savedPage.getSlug(),
                "Page updated successfully with " +
                        (savedPage.getSections() != null ? savedPage.getSections().size() : 0) +
                        " sections"
        );
    }



    @Override
    public void deletePage(String slug) {
        Page page = pageRepo.findBySlug(slug)
                .orElseThrow(() -> new NotFoundException("Page with slug '" + slug + "' not found."));

        try {
            if (page.getImageUrl() != null) {
                cloudAdapter.deleteFile(page.getImageUrl());
            }

            if (page.getDocumentUrl() != null) {
                cloudAdapter.deleteFile(page.getDocumentUrl());
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
