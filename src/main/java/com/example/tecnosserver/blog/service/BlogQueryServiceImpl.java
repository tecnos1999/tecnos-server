package com.example.tecnosserver.blog.service;

import com.example.tecnosserver.blog.dto.BlogDTO;
import com.example.tecnosserver.blog.mapper.BlogMapper;
import com.example.tecnosserver.blog.model.Blog;
import com.example.tecnosserver.blog.repo.BlogRepo;
import com.example.tecnosserver.exceptions.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class BlogQueryServiceImpl implements BlogQueryService {

    private final BlogRepo blogRepo;
    private final BlogMapper blogMapper;

    @Override
    public BlogDTO getBlogByCode(String code) {
        Blog blog = blogRepo.findByCode(code)
                .orElseThrow(() -> new NotFoundException("Blog not found with code: " + code));

        log.info("Retrieved blog by code: {}", code);
        return blogMapper.toDTO(blog);
    }

    @Override
    public List<BlogDTO> getAllBlogs() {
        List<Blog> blogs = blogRepo.findAll();

        log.info("Retrieved all blogs. Count: {}", blogs.size());
        return blogs.stream()
                .map(blogMapper::toDTO)
                .collect(Collectors.toList());
    }
}
