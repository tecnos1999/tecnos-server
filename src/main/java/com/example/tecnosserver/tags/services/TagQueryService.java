package com.example.tecnosserver.tags.services;
import com.example.tecnosserver.tags.dto.TagDTO;

import java.util.List;
import java.util.Optional;

public interface TagQueryService {
    Optional<TagDTO> findByName(String name);
    Optional<List<TagDTO>> findAllTags();
}

