package com.example.tecnosserver.page.service;

import com.example.tecnosserver.page.dto.CreatePageDTO;
import com.example.tecnosserver.page.dto.PageResponseDTO;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

public interface PageCommandService {

    PageResponseDTO createPage(CreatePageDTO createPageDTO, MultipartFile pageImage, Map<Integer, MultipartFile> sectionImages);


    void updatePage(String slug, CreatePageDTO createPageDTO, MultipartFile pageImage, Map<Integer, MultipartFile> sectionImages);


    void deletePage(String slug);
}
