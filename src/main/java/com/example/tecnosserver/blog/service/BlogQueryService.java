package com.example.tecnosserver.blog.service;

import com.example.tecnosserver.blog.dto.BlogDTO;

import java.util.List;

public interface BlogQueryService {
    BlogDTO getBlogByCode(String code);

    List<BlogDTO> getAllBlogs();

    List<BlogDTO> getBlogsByCodeIn(List<String> codes);
}
