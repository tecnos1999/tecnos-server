package com.example.tecnosserver.partners.service;
import com.example.tecnosserver.exceptions.exception.AlreadyExistsException;
import com.example.tecnosserver.exceptions.exception.AppException;
import com.example.tecnosserver.exceptions.exception.NotFoundException;
import com.example.tecnosserver.intercom.CloudAdapter;
import com.example.tecnosserver.partners.dto.PartnerDTO;
import com.example.tecnosserver.partners.mappers.PartnerMapper;
import com.example.tecnosserver.partners.model.Partner;
import com.example.tecnosserver.partners.repo.PartnerRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class PartnerCommandServiceImpl implements PartnerCommandService {

    private final PartnerRepo partnerRepository;
    private final PartnerMapper partnerMapper;
    private final CloudAdapter cloudAdapter;

    @Override
    public void addPartner(PartnerDTO partnerDTO) {
        validatePartnerDTO(partnerDTO);

        if (partnerRepository.findByName(partnerDTO.name()).isPresent()) {
            throw new AlreadyExistsException("Partner with name '" + partnerDTO.name() + "' already exists.");
        }

        Partner partner = partnerMapper.fromDTO(partnerDTO);
        partnerRepository.save(partner);
    }

    @Override
    public void deletePartner(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new AppException("Partner name cannot be null or empty.");
        }

        Partner partner = partnerRepository.findByName(name.trim())
                .orElseThrow(() -> new NotFoundException("Partner with name '" + name + "' not found."));

        List<String> fileUrls = new ArrayList<>();

        if (partner.getImage() != null && partner.getImage().getUrl() != null) {
            fileUrls.add(partner.getImage().getUrl());
        }

        if (partner.getCatalogFile() != null && !partner.getCatalogFile().trim().isEmpty()) {
            fileUrls.add(partner.getCatalogFile());
        }

        if (!fileUrls.isEmpty()) {
            try {
                cloudAdapter.deleteFiles(fileUrls);
            } catch (Exception e) {
                throw new AppException("Failed to delete associated files from cloud: " + e.getMessage());
            }
        }

        partnerRepository.delete(partner);
    }

    private void validatePartnerDTO(PartnerDTO partnerDTO) {
        if (partnerDTO == null) {
            throw new AppException("Partner data cannot be null.");
        }
        if (partnerDTO.name() == null || partnerDTO.name().trim().isEmpty()) {
            throw new AppException("Partner name cannot be null or empty.");
        }
        
        if (partnerDTO.description() == null || partnerDTO.description().trim().isEmpty()) {
            throw new AppException("Description cannot be null or empty.");
        }
    }
}

