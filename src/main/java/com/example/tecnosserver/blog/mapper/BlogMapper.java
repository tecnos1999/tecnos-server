package com.example.tecnosserver.blog.mapper;

import com.example.tecnosserver.blog.dto.BlogDTO;
import com.example.tecnosserver.blog.model.Blog;
import com.example.tecnosserver.caption.dto.CaptionDTO;
import com.example.tecnosserver.caption.model.Caption;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class BlogMapper {

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
                        ? blog.getCaptions().stream().map(this::toCaptionDTO).toList()
                        : List.of()
        );
    }

    public Blog fromDTO(BlogDTO blogDTO) {
        return Blog.builder()
                .code(blogDTO.code())
                .title(blogDTO.title())
                .description(blogDTO.description())
                .mainPhotoUrl(blogDTO.mainPhotoUrl())
                .broschureUrl(blogDTO.broschureUrl())
                .viewUrl(blogDTO.viewUrl())
                .active(blogDTO.active())
                .build();
    }
    
    private CaptionDTO toCaptionDTO(Caption caption) {
        return new CaptionDTO(
                caption.getCode(),
                caption.getText(),
                caption.getPosition(),
                caption.getPhotoUrl(),
                caption.isActive()
        );
    }
}
