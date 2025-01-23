package com.example.tecnosserver.page.service;

import com.example.tecnosserver.page.dto.PageDTO;

import java.util.Optional;


import java.util.List;
import java.util.Optional;

public interface PageQueryService {

    Optional<PageDTO> findPageBySlug(String slug);

    Optional<PageDTO> findPageByName(String name);


    List<PageDTO> findPagesBySlugList(List<String> slugs);


    List<PageDTO> getAllPages();
}

