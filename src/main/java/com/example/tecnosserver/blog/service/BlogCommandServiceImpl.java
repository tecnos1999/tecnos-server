package com.example.tecnosserver.blog.service;

import com.example.tecnosserver.blog.dto.BlogDTO;
import com.example.tecnosserver.blog.mapper.BlogMapper;
import com.example.tecnosserver.blog.model.Blog;
import com.example.tecnosserver.blog.repo.BlogRepo;
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
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

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

        if (mainPhoto != null && !mainPhoto.isEmpty()) {
            String photoUrl = uploadFile(mainPhoto, "Failed to upload main photo: ");
            blog.setMainPhotoUrl(photoUrl);
        }

        if (broschure != null && !broschure.isEmpty()) {
            String broschureUrl = uploadFile(broschure, "Failed to upload broschure: ");
            blog.setBroschureUrl(broschureUrl);
        }

        activateCaptions(blogDTO.captionCodes());
        blogRepo.save(blog);
    }

    @Override
    public void updateBlog(String blogCode, BlogDTO blogDTO, MultipartFile mainPhoto, MultipartFile broschure) {
        validateBlogDTO(blogDTO);

        Blog blog = blogRepo.findByCode(blogCode)
                .orElseThrow(() -> new NotFoundException("Blog with code '" + blogCode + "' not found."));

        if (mainPhoto != null && !mainPhoto.isEmpty()) {
            safeDeleteFile(blog.getMainPhotoUrl());
            String photoUrl = uploadFile(mainPhoto, "Failed to upload new main photo: ");
            blog.setMainPhotoUrl(photoUrl);
        }

        if (broschure != null && !broschure.isEmpty()) {
            safeDeleteFile(blog.getBroschureUrl());
            String broschureUrl = uploadFile(broschure, "Failed to upload new broschure: ");
            blog.setBroschureUrl(broschureUrl);
        }

        blog.setTitle(blogDTO.title());
        blog.setDescription(blogDTO.description());
        blog.setViewUrl(blogDTO.viewUrl());

        updateCaptions(blog, blogDTO.captionCodes());
        blogRepo.save(blog);
    }


    @Override
    public void deleteBlog(String blogCode) {
        if (blogCode == null || blogCode.trim().isEmpty()) {
            throw new AppException("Blog code cannot be null or empty.");
        }

        Blog blog = blogRepo.findByCode(blogCode.trim())
                .orElseThrow(() -> new NotFoundException("Blog with code '" + blogCode + "' not found."));

        deactivateCaptions(blog.getCaptions().stream()
                .map(Caption::getCode)
                .collect(Collectors.toList()));

        safeDeleteFile(blog.getMainPhotoUrl());

        blogRepo.delete(blog);
    }

    private void activateCaptions(List<String> captionCodes) {
        if (captionCodes != null && !captionCodes.isEmpty()) {
            List<Caption> captions = captionRepo.findByCodeIn(captionCodes)
                    .orElseThrow(() -> new NotFoundException("Some captions not found."));

            captions.forEach(caption -> caption.setActive(true));
            captionRepo.saveAll(captions);
        }
    }

    private void deactivateCaptions(List<String> captionCodes) {
        if (captionCodes != null && !captionCodes.isEmpty()) {
            List<Caption> captions = captionRepo.findByCodeIn(captionCodes)
                    .orElseThrow(() -> new NotFoundException("Some captions not found."));

            captions.forEach(caption -> caption.setActive(false));
            captionRepo.saveAll(captions);
        }
    }

    private void updateCaptions(Blog blog, List<String> newCaptionCodes) {
        Set<String> currentCaptionCodes = blog.getCaptions().stream()
                .map(Caption::getCode)
                .collect(Collectors.toSet());

        Set<String> newCaptionCodeSet = newCaptionCodes == null ? Set.of() : Set.copyOf(newCaptionCodes);

        List<String> captionsToActivate = newCaptionCodeSet.stream()
                .filter(code -> !currentCaptionCodes.contains(code))
                .collect(Collectors.toList());

        List<String> captionsToDeactivate = currentCaptionCodes.stream()
                .filter(code -> !newCaptionCodeSet.contains(code))
                .collect(Collectors.toList());

        activateCaptions(captionsToActivate);
        deactivateCaptions(captionsToDeactivate);
    }

    private void validateBlogDTO(BlogDTO blogDTO) {
        if (blogDTO == null) {
            throw new AppException("Blog data cannot be null.");
        }
        if (blogDTO.title() == null || blogDTO.title().trim().isEmpty()) {
            throw new AppException("Blog title cannot be null or empty.");
        }
        if (blogDTO.description() == null || blogDTO.description().trim().isEmpty()) {
            throw new AppException("Description cannot be null or empty.");
        }
    }

    private String uploadFile(MultipartFile file, String errorMessage) {
        if (file != null && !file.isEmpty()) {
            try {
                return cloudAdapter.uploadFile(file);
            } catch (Exception e) {
                throw new AppException(errorMessage + e.getMessage());
            }
        }
        return null;
    }

    private void safeDeleteFile(String fileUrl) {
        if (fileUrl != null && !fileUrl.isBlank()) {
            try {
                cloudAdapter.deleteFile(fileUrl);
                log.info("Deleted file from cloud: " + fileUrl);
            } catch (Exception e) {
                log.error("Failed to delete associated file: " + e.getMessage());
            }
        }
    }
}
