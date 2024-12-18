package com.example.tecnosserver.tags.services;


import com.example.tecnosserver.exceptions.exception.NotFoundException;
import com.example.tecnosserver.tags.dto.TagDTO;
import com.example.tecnosserver.tags.mapper.TagMapper;
import com.example.tecnosserver.tags.model.Tag;
import com.example.tecnosserver.tags.repo.TagRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TagQueryServiceImpl implements TagQueryService {

    private final TagRepo tagRepository;
    private final TagMapper tagMapper;

    @Override
    public Optional<TagDTO> findByName(String name) {
        return tagRepository.findByName(name)
                .map(tagMapper::toDTO);
    }

    @Override
    public Optional<List<TagDTO>> findAllTags() {
        List<Tag> tags = tagRepository.findAll();
        if (tags.isEmpty()) {
            throw new NotFoundException("No tags found.");
        }
        return Optional.of(tags.stream()
                .map(tagMapper::toDTO)
                .collect(Collectors.toList()));
    }
}

