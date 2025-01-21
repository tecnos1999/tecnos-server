package com.example.tecnosserver.page.service;

import com.example.tecnosserver.exceptions.exception.AlreadyExistsException;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
        Page existingPage = pageRepo.findBySlug(slug)
                .orElseThrow(() -> new NotFoundException("Page with slug '" + slug + "' not found."));

        existingPage.setTitle(createPageDTO.title());
        existingPage.setSubtitle(createPageDTO.subtitle());
        existingPage.setLink(createPageDTO.link());

        if (pageImage != null && !pageImage.isEmpty()) {
            if (existingPage.getImageUrl() != null) {
                cloudAdapter.deleteFile(existingPage.getImageUrl());
            }
            String newImageUrl = cloudAdapter.uploadFile(pageImage);
            existingPage.setImageUrl(newImageUrl);
        }

        if (createPageDTO.products() != null) {
            existingPage.getProducts().clear();
            List<Product> products = productRepo.findBySkuIn(createPageDTO.products());
            products.forEach(existingPage::addProduct);
        }

        if (createPageDTO.subPages() != null) {
            existingPage.getSubPages().clear();
            List<Page> subPages = pageRepo.findBySlugIn(createPageDTO.subPages()).get();
            if (subPages.isEmpty()) {
                throw new NotFoundException("Sub-pages not found.");
            }
            existingPage.setSubPages(subPages);
        }

        if (createPageDTO.sections() != null) {
            existingPage.getSections().forEach(section -> {
                if (section.getImageUrl() != null) {
                    cloudAdapter.deleteFile(section.getImageUrl());
                }
            });

            sectionRepo.deleteAll(existingPage.getSections());
            existingPage.getSections().clear();

            List<Section> newSections = new ArrayList<>();
            for (int i = 0; i < createPageDTO.sections().size(); i++) {
                CreateSectionDTO sectionDTO = createPageDTO.sections().get(i);
                Section section = pageMapper.createSection(sectionDTO, existingPage);

                MultipartFile sectionImage = sectionImages.get(i);
                if (sectionImage != null && !sectionImage.isEmpty()) {
                    String sectionImageUrl = cloudAdapter.uploadFile(sectionImage);
                    section.setImageUrl(sectionImageUrl);
                }

                newSections.add(section);
            }

            sectionRepo.saveAll(newSections);
            existingPage.setSections(newSections);
        }

        pageRepo.save(existingPage);
    }

    @Override
    public void deletePage(String slug) {
        Page page = pageRepo.findBySlug(slug)
                .orElseThrow(() -> new NotFoundException("Page with slug '" + slug + "' not found."));

        if (page.getImageUrl() != null) {
            cloudAdapter.deleteFile(page.getImageUrl());
        }

        if (page.getSections() != null) {
            page.getSections().forEach(section -> {
                if (section.getImageUrl() != null) {
                    cloudAdapter.deleteFile(section.getImageUrl());
                }
            });
        }

        pageRepo.delete(page);
    }
}
