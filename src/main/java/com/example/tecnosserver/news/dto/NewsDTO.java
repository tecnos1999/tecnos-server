package com.example.tecnosserver.news.dto;


import com.example.tecnosserver.tags.dto.TagDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NewsDTO {
    private String code;
    private String title;
    private String shortDescription;
    private String longDescription;
    private List<TagDTO> tags;
    private String icon;
}

