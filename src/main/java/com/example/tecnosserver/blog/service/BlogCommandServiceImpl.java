package com.example.tecnosserver.blog.service;


import com.example.tecnosserver.blog.dto.BlogRequestDTO;
import com.example.tecnosserver.blog.model.Blog;
import com.example.tecnosserver.blog.repo.BlogRepo;
import com.example.tecnosserver.caption.model.Caption;
import com.example.tecnosserver.caption.repo.CaptionRepo;
import com.example.tecnosserver.exceptions.exception.NotFoundException;
import com.example.tecnosserver.intercom.CloudAdapter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class BlogCommandServiceImpl implements BlogCommandService {

    private final BlogRepo blogRepository;
    private final CaptionRepo captionRepository;
    private final CloudAdapter cloudAdapter;

    @Override
    public void addBlog(BlogRequestDTO blogRequestDTO) {
        String mainPhotoUrl = uploadFile(blogRequestDTO.mainPhoto());

        Blog blog = Blog.builder()
                .title(blogRequestDTO.title())
                .description(blogRequestDTO.description())
                .mainPhotoUrl(mainPhotoUrl)
                .broschureUrl(blogRequestDTO.broschureUrl())
                .build();

        List<Caption> captions = new ArrayList<>();
        if (blogRequestDTO.captions() != null) {
            for (var captionDTO : blogRequestDTO.captions()) {
                String captionPhotoUrl = uploadFile(captionDTO.photo());

                Caption caption = Caption.builder()
                        .text(captionDTO.text())
                        .photoUrl(captionPhotoUrl)
                        .position(captionDTO.position())
                        .build();

                captions.add(caption);
            }
        }

        blog.setCaptions(captions);
        blogRepository.save(blog);
    }

    @Override
    public void updateBlog(String blogCode, BlogRequestDTO blogRequestDTO) {
        Blog blog = blogRepository.findByCode(blogCode)
                .orElseThrow(() -> new NotFoundException("Blog with code '" + blogCode + "' not found."));

        String mainPhotoUrl = uploadFile(blogRequestDTO.mainPhoto());
        if (mainPhotoUrl != null) {
            safeDeleteFile(blog.getMainPhotoUrl());
            blog.setMainPhotoUrl(mainPhotoUrl);
        }

        blog.setTitle(blogRequestDTO.title());
        blog.setDescription(blogRequestDTO.description());
        blog.setBroschureUrl(blogRequestDTO.broschureUrl());

        List<Caption> captions = new ArrayList<>();
        if (blogRequestDTO.captions() != null) {
            captionRepository.deleteAll(blog.getCaptions());

            for (var captionDTO : blogRequestDTO.captions()) {
                String captionPhotoUrl = uploadFile(captionDTO.photo());

                Caption caption = Caption.builder()
                        .text(captionDTO.text())
                        .photoUrl(captionPhotoUrl)
                        .position(captionDTO.position())
                        .build();

                captions.add(caption);
            }
        }

        blog.setCaptions(captions);
        blogRepository.save(blog);
    }

    @Override
    public void deleteBlog(String blogCode) {
        Blog blog = blogRepository.findByCode(blogCode)
                .orElseThrow(() -> new NotFoundException("Blog with code '" + blogCode + "' not found."));

        safeDeleteFile(blog.getMainPhotoUrl());
        blog.getCaptions().forEach(caption -> safeDeleteFile(caption.getPhotoUrl()));

        blogRepository.delete(blog);
    }

    private String uploadFile(MultipartFile file) {
        if (file != null && !file.isEmpty()) {
            return cloudAdapter.uploadFile(file);
        }
        return null;
    }

    private void safeDeleteFile(String fileUrl) {
        if (fileUrl != null && !fileUrl.isBlank()) {
            cloudAdapter.deleteFile(fileUrl);
        }
    }
}
