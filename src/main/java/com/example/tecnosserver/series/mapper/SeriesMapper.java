package com.example.tecnosserver.series.mapper;


import com.example.tecnosserver.blog.model.Blog;
import com.example.tecnosserver.series.dto.SeriesDTO;
import com.example.tecnosserver.series.model.Series;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class SeriesMapper {

    public SeriesDTO toDTO(Series series) {
        return new SeriesDTO(
                series.getCode(),
                series.getName(),
                series.getDescription(),
                series.getImageUrl(),
                series.getBlogs() != null
                        ? series.getBlogs().stream().map(Blog::getCode).collect(Collectors.toList())
                        : List.of()
        );
    }

    public Series fromDTO(SeriesDTO seriesDTO) {
        return Series.builder()
                .code(seriesDTO.code())
                .name(seriesDTO.name())
                .description(seriesDTO.description())
                .imageUrl(seriesDTO.imageUrl())
                .build();
    }
}

