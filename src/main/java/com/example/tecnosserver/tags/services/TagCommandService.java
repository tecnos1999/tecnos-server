package com.example.tecnosserver.tags.services;

import com.example.tecnosserver.tags.dto.TagDTO;

public interface TagCommandService {
    void addTag(TagDTO tagDTO);
    void updateTag(String name, TagDTO tagDTO);
    void deleteTag(String name);
}
