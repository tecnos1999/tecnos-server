package com.example.tecnosserver.page.service;

import com.example.tecnosserver.page.dto.PageDTO;

import java.util.Optional;


import java.util.List;
import java.util.Optional;

public interface PageQueryService {
    /**
     * Find a page by its slug
     * @param slug The page slug
     * @return Optional containing the page if found
     */
    Optional<PageDTO> findPageBySlug(String slug);

    /**
     * Find pages by a list of slugs
     * @param slugs List of page slugs to find
     * @return List of found pages
     */
    List<PageDTO> findPagesBySlugList(List<String> slugs);

    /**
     * Get all pages
     * @return List of all pages
     */
    List<PageDTO> getAllPages();
}

