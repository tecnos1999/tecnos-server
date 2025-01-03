package com.example.tecnosserver.blog.dto;


import com.example.tecnosserver.caption.dto.CaptionDTO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public record BlogRequestDTO(
        String title,
        String description,
        MultipartFile mainPhoto,
        String broschureUrl,
        String videoUrl,
        List<CaptionDTO> captions
) {

}
