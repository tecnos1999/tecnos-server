package com.example.tecnosserver.page.service;

import com.example.tecnosserver.page.dto.CreatePageDTO;
import com.example.tecnosserver.page.dto.PageResponseDTO;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

public interface PageCommandService {

    PageResponseDTO createPage(CreatePageDTO createPageDTO, MultipartFile pageImage,MultipartFile document, Map<String, MultipartFile> sectionImages);



    PageResponseDTO updatePage(String slug, CreatePageDTO createPageDTO, MultipartFile pageImage, MultipartFile document, Map<String, MultipartFile> sectionImagesMap);


    void deletePage(String slug);
}
