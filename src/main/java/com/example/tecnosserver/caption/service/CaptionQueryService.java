package com.example.tecnosserver.caption.service;

import com.example.tecnosserver.caption.dto.CaptionDTO;

import java.util.List;

public interface CaptionQueryService {

    CaptionDTO getCaptionByCode(String code);

    List<CaptionDTO> getAllCaptions();
}

