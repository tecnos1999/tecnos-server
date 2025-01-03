package com.example.tecnosserver.blog.service;

import com.example.tecnosserver.blog.dto.BlogDTO;
import org.springframework.web.multipart.MultipartFile;

public interface BlogCommandService {
    void addBlog(BlogDTO blogRequestDTO , MultipartFile file);

    void updateBlog(String blogCode, BlogDTO blogRequestDTO , MultipartFile file);

    void deleteBlog(String blogCode);
}
