package com.example.tecnosserver.blog.service;

import com.example.tecnosserver.blog.dto.BlogDTO;
import org.springframework.web.multipart.MultipartFile;

public interface BlogCommandService {
    void addBlog(BlogDTO blogDTO, MultipartFile mainPhoto, MultipartFile broschure);

    void updateBlog(String blogCode, BlogDTO blogDTO, MultipartFile mainPhoto, MultipartFile broschure);

    void deleteBlog(String blogCode);
}
