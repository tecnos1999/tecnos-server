package com.example.tecnosserver.caption.mapper;

import com.example.tecnosserver.caption.dto.CaptionDTO;
import com.example.tecnosserver.caption.model.Caption;
import org.springframework.stereotype.Component;

@Component
public class CaptionMapper {

    public CaptionDTO toDTO(Caption caption) {
        return new CaptionDTO(
                caption.getCode(),
                caption.getText(),
                caption.getPosition(),
                caption.getPhotoUrl(),
                caption.isActive()
        );
    }

    public Caption fromDTO(CaptionDTO captionDTO) {
        return Caption.builder()
                .text(captionDTO.text())
                .position(captionDTO.position())
                .photoUrl(captionDTO.photoUrl())
                .active(captionDTO.active())
                .build();
    }
}
