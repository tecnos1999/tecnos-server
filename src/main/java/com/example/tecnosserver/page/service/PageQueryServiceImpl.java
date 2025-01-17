package com.example.tecnosserver.page.service;

import com.example.tecnosserver.exceptions.exception.NotFoundException;
import com.example.tecnosserver.page.dto.PageDTO;
import com.example.tecnosserver.page.mapper.PageMapper;
import com.example.tecnosserver.page.repo.PageRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PageQueryServiceImpl implements PageQueryService {

    private final PageRepo pageRepo;
    private final PageMapper pageMapper;

    @Override
    public Optional<PageDTO> findPageBySlug(String slug) {
        if (slug == null || slug.trim().isEmpty()) {
            throw new IllegalArgumentException("Slug cannot be null or empty.");
        }

        return pageRepo.findBySlug(slug)
                .map(pageMapper::toDTO)
                .or(() -> {
                    throw new NotFoundException("Page with slug '" + slug + "' not found.");
                });
    }

}
