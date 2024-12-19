package com.example.tecnosserver.partners.mapper;

import com.example.tecnosserver.image.mapper.ImageMapper;
import com.example.tecnosserver.partners.dto.PartnerDTO;
import com.example.tecnosserver.partners.model.Partner;
import org.springframework.stereotype.Component;

@Component
public class PartnerMapper {

    private final ImageMapper imageMapper;

    public PartnerMapper(ImageMapper imageMapper) {
        this.imageMapper = imageMapper;
    }

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
                .image(partner.getImage() != null ? imageMapper.mapImageToDTO(partner.getImage()) : null)
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
                .image(partnerDTO.image() != null ? ImageMapper.mapDTOToImage(partnerDTO.image()) : null)
                .build();
    }
}
