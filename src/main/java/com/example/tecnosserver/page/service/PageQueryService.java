package com.example.tecnosserver.page.service;

import com.example.tecnosserver.page.dto.PageDTO;

import java.util.Optional;

public interface PageQueryService {

    Optional<PageDTO> findPageBySlug(String slug);

}
