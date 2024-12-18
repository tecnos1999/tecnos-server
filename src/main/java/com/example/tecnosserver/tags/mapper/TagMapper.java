package com.example.tecnosserver.tags.mapper;


import com.example.tecnosserver.tags.dto.TagDTO;
import com.example.tecnosserver.tags.model.Tag;
import org.springframework.stereotype.Component;

@Component
public class TagMapper {
    public Tag fromDTO(TagDTO tagDTO) {
        return Tag.builder()
                .name(tagDTO.name())
                .color(tagDTO.color())
                .build();
    }

    public TagDTO toDTO(Tag tag) {
        return TagDTO.builder()
                .name(tag.getName())
                .color(tag.getColor())
                .build();
    }
}

