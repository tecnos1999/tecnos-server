package com.example.tecnosserver.news.mapper;


import com.example.tecnosserver.news.dto.NewsDTO;
import com.example.tecnosserver.news.model.News;
import com.example.tecnosserver.tags.mapper.TagMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class NewsMapper {

    private final TagMapper tagMapper;

    public News fromDTO(NewsDTO dto) {
        return News.builder()
                .code(dto.getCode())
                .title(dto.getTitle())
                .shortDescription(dto.getShortDescription())
                .longDescription(dto.getLongDescription())
                .tags(dto.getTags().stream().map(tagMapper::fromDTO).toList())
                .icon(dto.getIcon())
                .build();
    }

    public NewsDTO toDTO(News news) {
        return NewsDTO.builder()
                .code(news.getCode())
                .title(news.getTitle())
                .shortDescription(news.getShortDescription())
                .longDescription(news.getLongDescription())
                .tags(news.getTags().stream().map(tagMapper::toDTO).toList())
                .icon(news.getIcon())
                .build();
    }
}

