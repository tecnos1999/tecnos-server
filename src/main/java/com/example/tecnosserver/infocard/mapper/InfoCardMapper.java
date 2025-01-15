package com.example.tecnosserver.infocard.mapper;

import com.example.tecnosserver.infocard.dto.InfoCardDTO;
import com.example.tecnosserver.infocard.model.InfoCard;
import org.springframework.stereotype.Component;

@Component
public class InfoCardMapper {

    public InfoCardDTO toDTO(InfoCard infoCard) {
        return new InfoCardDTO(
                infoCard.getCode(),
                infoCard.getTitle(),
                infoCard.getDescription(),
                infoCard.getFeatures(),
                infoCard.isActive(),
                infoCard.getCreatedAt(),
                infoCard.getUpdatedAt()
        );
    }

    public InfoCard fromDTO(InfoCardDTO infoCardDTO) {
        return InfoCard.builder()
                .code(infoCardDTO.code())
                .title(infoCardDTO.title())
                .description(infoCardDTO.description())
                .features(infoCardDTO.features())
                .active(infoCardDTO.active())
                .build();
    }
}
