package com.example.tecnosserver.caption.service;

import com.example.tecnosserver.caption.dto.CaptionDTO;
import org.springframework.web.multipart.MultipartFile;

public interface CaptionCommandService {

    void addCaption(CaptionDTO captionDTO, MultipartFile file);

    void updateCaption(String code, CaptionDTO captionDTO, MultipartFile file);

    void deleteCaption(String code);


}
