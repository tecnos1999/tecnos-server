package com.example.tecnosserver.tags.service;

import com.example.tecnosserver.exceptions.exception.AlreadyExistsException;
import com.example.tecnosserver.exceptions.exception.NotFoundException;
import com.example.tecnosserver.tags.dto.TagDTO;
import com.example.tecnosserver.tags.mapper.TagMapper;
import com.example.tecnosserver.tags.model.Tag;
import com.example.tecnosserver.tags.repo.TagRepo;
import com.example.tecnosserver.tags.services.TagCommandService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class TagCommandServiceImpl implements TagCommandService {

    private final TagRepo tagRepository;
    private final TagMapper tagMapper;

    @Override
    public void addTag(TagDTO tagDTO) {
        if (tagRepository.findByName(tagDTO.name()).isPresent()) {
            throw new AlreadyExistsException("Tag with name '" + tagDTO.name() + "' already exists.");
        }
        Tag tag = tagMapper.fromDTO(tagDTO);
        tagRepository.save(tag);
    }

    @Override
    public void updateTag(String name, TagDTO tagDTO) {
        Tag tag = tagRepository.findByName(name)
                .orElseThrow(() -> new NotFoundException("Tag with name '" + name + "' not found."));
        tag.setName(tagDTO.name());
        tag.setColor(tagDTO.color());
        tagRepository.save(tag);
    }

    @Override
    public void deleteTag(String name) {
        Tag tag = tagRepository.findByName(name)
                .orElseThrow(() -> new NotFoundException("Tag with name '" + name + "' not found."));
        tagRepository.delete(tag);
    }
}
