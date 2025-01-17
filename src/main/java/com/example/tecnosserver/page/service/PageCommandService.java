package com.example.tecnosserver.page.service;

import com.example.tecnosserver.page.dto.PageDTO;

public interface PageCommandService {

    void createPage(PageDTO pageDTO);

    void updatePage(String slug, PageDTO pageDTO);

    void deletePage(String slug);
}

