package com.example.tecnosserver.caption.service;

import com.example.tecnosserver.caption.dto.CaptionDTO;
import com.example.tecnosserver.caption.mapper.CaptionMapper;
import com.example.tecnosserver.caption.model.Caption;
import com.example.tecnosserver.caption.repo.CaptionRepo;
import com.example.tecnosserver.exceptions.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class CaptionQueryServiceImpl implements CaptionQueryService {

    private final CaptionRepo captionRepo;
    private final CaptionMapper captionMapper;

    @Override
    public CaptionDTO getCaptionByCode(String code) {
        Caption caption = captionRepo.findByCode(code)
                .orElseThrow(() -> new NotFoundException("Caption not found with code: " + code));

        log.info("Retrieved caption by code: {}", code);
        return captionMapper.toDTO(caption);
    }

    @Override
    public List<CaptionDTO> getAllCaptions() {
        List<Caption> captions = captionRepo.findAll();

        log.info("Retrieved all captions. Count: {}", captions.size());
        return captions.stream()
                .map(captionMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<CaptionDTO> getCaptionsByCodeIn(List<String> codes) {
        List<Caption> captions = captionRepo.findByCodeIn(codes)
                .orElseThrow(() -> new NotFoundException("Captions not found with codes: " + codes));

        log.info("Retrieved captions by codes: {}", codes);
        return captions.stream()
                .map(captionMapper::toDTO)
                .collect(Collectors.toList());
    }
}
