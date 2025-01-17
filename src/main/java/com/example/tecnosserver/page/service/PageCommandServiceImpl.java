package com.example.tecnosserver.page.service;

import com.example.tecnosserver.exceptions.exception.AlreadyExistsException;
import com.example.tecnosserver.exceptions.exception.NotFoundException;
import com.example.tecnosserver.page.dto.PageDTO;
import com.example.tecnosserver.page.mapper.PageMapper;
import com.example.tecnosserver.page.model.Page;
import com.example.tecnosserver.page.repo.PageRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class PageCommandServiceImpl implements PageCommandService {

    private final PageRepo pageRepo;
    private final PageMapper pageMapper;

    @Override
    public void createPage(PageDTO pageDTO) {
        if (pageRepo.findBySlug(pageDTO.slug()).isPresent()) {
            throw new AlreadyExistsException("Page with slug '" + pageDTO.slug() + "' already exists.");
        }

        Page page = pageMapper.fromDTO(pageDTO);
        pageRepo.save(page);
    }

    @Override
    public void updatePage(String slug, PageDTO pageDTO) {
        Page existingPage = pageRepo.findBySlug(slug)
                .orElseThrow(() -> new NotFoundException("Page with slug '" + slug + "' not found."));

        existingPage.setTitle(pageDTO.title());
        existingPage.setSubtitle(pageDTO.subtitle());
        existingPage.setImageUrl(pageDTO.imageUrl());
        existingPage.setLink(pageDTO.link());
        if (pageDTO.sections() != null) {
            existingPage.setSections(pageMapper.fromDTO(pageDTO).getSections());
        }
        if (pageDTO.subPages() != null) {
            existingPage.setSubPages(pageMapper.fromDTO(pageDTO).getSubPages());
        }

        pageRepo.save(existingPage);
    }

    @Override
    public void deletePage(String slug) {
        Page page = pageRepo.findBySlug(slug)
                .orElseThrow(() -> new NotFoundException("Page with slug '" + slug + "' not found."));

        pageRepo.delete(page);
    }
}
