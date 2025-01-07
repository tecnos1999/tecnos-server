package com.example.tecnosserver.blog.service;

import com.example.tecnosserver.blog.dto.BlogDTO;
import com.example.tecnosserver.blog.mapper.BlogMapper;
import com.example.tecnosserver.blog.model.Blog;
import com.example.tecnosserver.blog.repo.BlogRepo;
import com.example.tecnosserver.caption.dto.CaptionDTO;
import com.example.tecnosserver.caption.model.Caption;
import com.example.tecnosserver.caption.repo.CaptionRepo;
import com.example.tecnosserver.exceptions.exception.AlreadyExistsException;
import com.example.tecnosserver.exceptions.exception.AppException;
import com.example.tecnosserver.exceptions.exception.NotFoundException;
import com.example.tecnosserver.intercom.CloudAdapter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class BlogCommandServiceImpl implements BlogCommandService {

    private final BlogRepo blogRepo;
    private final CaptionRepo captionRepo;
    private final BlogMapper blogMapper;
    private final CloudAdapter cloudAdapter;
    @Override
    public void addBlog(BlogDTO blogDTO, MultipartFile mainPhoto, MultipartFile broschure) {
        validateBlogDTO(blogDTO);

        if (blogRepo.findByCode(blogDTO.code()).isPresent()) {
            throw new AlreadyExistsException("Blog with code '" + blogDTO.code() + "' already exists.");
        }

        Blog blog = blogMapper.fromDTO(blogDTO);
        blog.setMainPhotoUrl(uploadFile(mainPhoto, "Failed to upload main photo: "));
        blog.setBroschureUrl(uploadFile(broschure, "Failed to upload broschure: "));

        blogRepo.save(blog);

        associateExistingCaptions(blog, blogDTO.captions());
    }

    @Override
    public void updateBlog(String blogCode, BlogDTO blogDTO, MultipartFile mainPhoto, MultipartFile broschure) {
        validateBlogDTO(blogDTO);

        Blog blog = blogRepo.findByCode(blogCode)
                .orElseThrow(() -> new NotFoundException("Blog with code '" + blogCode + "' not found."));

        if (mainPhoto != null && !mainPhoto.isEmpty()) {
            blog.setMainPhotoUrl(uploadFile(mainPhoto, "Failed to upload new main photo: "));
        }

        if (broschure != null && !broschure.isEmpty()) {
            blog.setBroschureUrl(uploadFile(broschure, "Failed to upload new broschure: "));
        }

        blog.setTitle(blogDTO.title());
        blog.setDescription(blogDTO.description());
        blog.setViewUrl(blogDTO.viewUrl());

        associateExistingCaptions(blog, blogDTO.captions());

        blogRepo.save(blog);
    }

    @Override
    public void deleteBlog(String blogCode) {
        Blog blog = blogRepo.findByCode(blogCode.trim())
                .orElseThrow(() -> new NotFoundException("Blog with code '" + blogCode + "' not found."));

        captionRepo.deleteAll(blog.getCaptions());
        blogRepo.delete(blog);
    }

    private void associateExistingCaptions(Blog blog, List<CaptionDTO> captionDTOs) {
        if (captionDTOs != null && !captionDTOs.isEmpty()) {
            List<Caption> captions = captionDTOs.stream()
                    .map(dto -> captionRepo.findByCode(dto.code())
                            .map(existingCaption -> {
                                existingCaption.setBlog(blog);
                                existingCaption.setActive(true);
                                return existingCaption;
                            })
                            .orElseThrow(() -> new NotFoundException("Caption with code '" + dto.code() + "' not found")))
                    .toList();

            captionRepo.saveAll(captions);
        }
    }

    private void validateBlogDTO(BlogDTO blogDTO) {
        if (blogDTO == null || blogDTO.title() == null || blogDTO.title().trim().isEmpty()) {
            throw new AppException("Blog title cannot be null or empty.");
        }
        if (blogDTO.description() == null || blogDTO.description().trim().isEmpty()) {
            throw new AppException("Description cannot be null or empty.");
        }
    }

    private String uploadFile(MultipartFile file, String errorMessage) {
        try {
            return cloudAdapter.uploadFile(file);
        } catch (Exception e) {
            log.error("{} {}", errorMessage, e.getMessage());
            throw new AppException(errorMessage + e.getMessage());
        }
    }

}
