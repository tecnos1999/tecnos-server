package com.example.tecnosserver.blog.mapper;

import com.example.tecnosserver.blog.dto.BlogDTO;
import com.example.tecnosserver.blog.model.Blog;
import com.example.tecnosserver.caption.model.Caption;
import com.example.tecnosserver.caption.repo.CaptionRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@RequiredArgsConstructor
@Component
public class BlogMapper {

    private final CaptionRepo captionRepo;

    public BlogDTO toDTO(Blog blog) {
        return new BlogDTO(
                blog.getCode(),
                blog.getTitle(),
                blog.getDescription(),
                blog.getMainPhotoUrl(),
                blog.getBroschureUrl(),
                blog.getViewUrl(),
                blog.getSeries() != null ? blog.getSeries().getCode() : null,
                blog.isActive(),
                blog.getCaptions() != null
                        ? blog.getCaptions().stream().map(Caption::getCode).toList()
                        : List.of()
        );
    }

    public Blog fromDTO(BlogDTO blogDTO) {
        Blog blog = Blog.builder()
                .code(blogDTO.code())
                .title(blogDTO.title())
                .description(blogDTO.description())
                .mainPhotoUrl(blogDTO.mainPhotoUrl())
                .broschureUrl(blogDTO.broschureUrl())
                .viewUrl(blogDTO.viewUrl())
                .active(blogDTO.active())
                .build();

        if (blogDTO.captionCodes() != null && !blogDTO.captionCodes().isEmpty()) {
            List<Caption> captions = captionRepo.findByCodeIn(blogDTO.captionCodes())
                    .orElseThrow(() -> new RuntimeException("Some captions not found"));
            blog.setCaptions(captions);
        }

        return blog;
    }
}
