package com.example.tecnosserver.partners.service;

import com.example.tecnosserver.exceptions.exception.AppException;
import com.example.tecnosserver.exceptions.exception.NotFoundException;
import com.example.tecnosserver.partners.dto.PartnerDTO;
import com.example.tecnosserver.partners.mapper.PartnerMapper;
import com.example.tecnosserver.partners.model.Partner;
import com.example.tecnosserver.partners.repo.PartnerRepo;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PartnerQueryServiceImpl implements PartnerQueryService {

    private final PartnerRepo partnerRepo;
    private final PartnerMapper partnerMapper;

    public PartnerQueryServiceImpl(PartnerRepo partnerRepo, PartnerMapper partnerMapper) {
        this.partnerRepo = partnerRepo;
        this.partnerMapper = partnerMapper;
    }

    @Override
    public Optional<PartnerDTO> findPartnerByName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new AppException("Partner name cannot be null or empty");
        }
        Optional<Partner> partner = partnerRepo.findByName(name);
        if (partner.isEmpty()) {
            throw new NotFoundException("Partner with name " + name + " not found");
        }
        return partner.map(partnerMapper::toDTO);
    }

    @Override
    public Optional<List<PartnerDTO>> findAllPartners() {
        List<Partner> partners = partnerRepo.findAll();

        if (partners.isEmpty()) {
            throw new NotFoundException("No partners found");
        }

        List<PartnerDTO> partnerDTOs = partners.stream()
                .map(partnerMapper::toDTO)
                .collect(Collectors.toList());

        return Optional.of(partnerDTOs);
    }


}
