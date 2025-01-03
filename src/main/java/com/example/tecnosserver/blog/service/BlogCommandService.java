package com.example.tecnosserver.blog.service;


import com.example.tecnosserver.blog.dto.BlogRequestDTO;

public interface BlogCommandService {

    void addBlog(BlogRequestDTO blogRequestDTO);

    void updateBlog(String blogCode, BlogRequestDTO blogRequestDTO);

    void deleteBlog(String blogCode);
}

