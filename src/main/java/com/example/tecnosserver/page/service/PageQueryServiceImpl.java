package com.example.tecnosserver.page.service;

import com.example.tecnosserver.exceptions.exception.AppException;
import com.example.tecnosserver.exceptions.exception.NotFoundException;
import com.example.tecnosserver.page.dto.PageDTO;
import com.example.tecnosserver.page.mapper.PageMapper;
import com.example.tecnosserver.page.model.Page;
import com.example.tecnosserver.page.repo.PageRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class PageQueryServiceImpl implements PageQueryService {

    private final PageRepo pageRepo;
    private final PageMapper pageMapper;

    @Override
    public List<PageDTO> getAllPages() {
        log.info("Fetching all pages");
        try {
            List<Page> pages = pageRepo.findAll();
            if (pages.isEmpty()) {
                throw new NotFoundException("No pages found in the system.");
            }

            return pages.stream()
                    .map(pageMapper::toDTO)
                    .collect(Collectors.toList());
        } catch (NotFoundException e) {
            log.error("No pages found: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("Error while fetching all pages: {}", e.getMessage(), e);
            throw new AppException("Failed to retrieve pages: " + e.getMessage());
        }
    }

    @Override
    public Optional<PageDTO> findPageBySlug(String slug) {
        log.info("Searching for page with slug: {}", slug);
        try {
            return pageRepo.findBySlug(slug)
                    .map(pageMapper::toDTO);
        } catch (Exception e) {
            log.error("Error while fetching page with slug {}: {}", slug, e.getMessage(), e);
            throw new AppException("Failed to retrieve page: " + e.getMessage());
        }
    }

    @Override
    public List<PageDTO> findPagesBySlugList(List<String> slugs) {
        log.info("Searching for pages with slugs: {}", slugs);
        try {
            if (slugs == null || slugs.isEmpty()) {
                throw new AppException("Slug list cannot be null or empty");
            }

            List<Page> pages = pageRepo.findBySlugIn(slugs)
                    .orElseThrow(() -> new NotFoundException("No pages found for the provided slugs"));

            if (pages.isEmpty()) {
                throw new NotFoundException("No pages found for the provided slugs");
            }

            List<PageDTO> pageDTOs = pages.stream()
                    .map(pageMapper::toDTO)
                    .collect(Collectors.toList());

            List<String> foundSlugs = pageDTOs.stream()
                    .map(PageDTO::slug)
                    .toList();

            List<String> missingSlugs = slugs.stream()
                    .filter(slug -> !foundSlugs.contains(slug))
                    .collect(Collectors.toList());

            if (!missingSlugs.isEmpty()) {
                throw new NotFoundException(
                        String.format("Pages not found for slugs: %s", String.join(", ", missingSlugs))
                );
            }

            return pageDTOs;
        } catch (NotFoundException e) {
            log.error("Pages not found: {}", e.getMessage());
            throw e;
        } catch (AppException e) {
            log.error("Application error: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("Error while fetching pages with slugs {}: {}", slugs, e.getMessage(), e);
            throw new AppException("Failed to retrieve pages: " + e.getMessage());
        }
    }
}
