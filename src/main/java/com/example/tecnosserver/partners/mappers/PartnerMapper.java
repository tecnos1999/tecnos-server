package com.example.tecnosserver.partners.mappers;

import com.example.tecnosserver.image.dto.ImageDTO;
import com.example.tecnosserver.partners.dto.PartnerDTO;
import com.example.tecnosserver.partners.model.Partner;
import org.springframework.stereotype.Component;

@Component
public class PartnerMapper {

    public PartnerDTO toDTO(Partner partner) {
        if (partner == null) {
            return null;
        }

        return PartnerDTO.builder()
                .name(partner.getName())
                .description(partner.getDescription())
                .catalogFile(partner.getCatalogFile())
                .createdAt(partner.getCreatedAt())
                .updatedAt(partner.getUpdatedAt())
                .image(partner.getImage() != null
                        ? ImageDTO.builder()
                        .url(partner.getImage().getUrl())
                        .type(partner.getImage().getType())
                        .build()
                        : null)
                .build();
    }

    public Partner fromDTO(PartnerDTO partnerDTO) {
        if (partnerDTO == null) {
            return null;
        }

        return Partner.builder()
                .name(partnerDTO.name())
                .description(partnerDTO.description())
                .catalogFile(partnerDTO.catalogFile())
                .image(partnerDTO.image() != null
                        ? com.example.tecnosserver.image.model.Image.builder()
                        .url(partnerDTO.image().getUrl())
                        .type(partnerDTO.image().getType())
                        .build()
                        : null)
                .build();
    }
}
