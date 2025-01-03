package com.example.tecnosserver.blog.mapper;

import com.example.tecnosserver.blog.dto.BlogDTO;
import com.example.tecnosserver.blog.model.Blog;
import com.example.tecnosserver.caption.model.Caption;
import com.example.tecnosserver.caption.repo.CaptionRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
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
                blog.getSeries().getCode(),
                blog.getCaptions().stream()
                        .map(Caption::getCode)
                        .toList()
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
                .build();

        if (blogDTO.captionCodes() != null && !blogDTO.captionCodes().isEmpty()) {
            List<Caption> retrievedCaptions = captionRepo.findByCodeIn(blogDTO.captionCodes())
                    .orElse(List.of());
            blog.setCaptions(retrievedCaptions);
        }

        return blog;
    }

}
