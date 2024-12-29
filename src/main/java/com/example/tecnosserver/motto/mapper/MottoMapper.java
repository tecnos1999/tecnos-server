package com.example.tecnosserver.motto.mapper;

import com.example.tecnosserver.motto.dto.MottoDTO;
import com.example.tecnosserver.motto.model.Motto;
import org.springframework.stereotype.Component;

@Component
public class MottoMapper {

    public MottoDTO toDTO(Motto motto) {
        if (motto == null) {
            return null;
        }

        return new MottoDTO(
                motto.getCode(),
                motto.getContent(),
                motto.getCreatedAt(),
                motto.getUpdatedAt()
        );
    }

    public Motto toEntity(MottoDTO mottoDTO) {
        if (mottoDTO == null) {
            return null;
        }

        return Motto.builder()
                .code(mottoDTO.code())
                .content(mottoDTO.content())
                .createdAt(mottoDTO.createdAt())
                .updatedAt(mottoDTO.updatedAt())
                .build();
    }
}
