package com.example.tecnosserver.partners.service;
import com.example.tecnosserver.exceptions.exception.AlreadyExistsException;
import com.example.tecnosserver.exceptions.exception.AppException;
import com.example.tecnosserver.exceptions.exception.NotFoundException;
import com.example.tecnosserver.partners.dto.PartnerDTO;
import com.example.tecnosserver.partners.mappers.PartnerMapper;
import com.example.tecnosserver.partners.model.Partner;
import com.example.tecnosserver.partners.repo.PartnerRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class PartnerCommandServiceImpl implements PartnerCommandService {

    private final PartnerRepo partnerRepository;
    private final PartnerMapper partnerMapper;

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

        partnerRepository.delete(partner);
    }

    private void validatePartnerDTO(PartnerDTO partnerDTO) {
        if (partnerDTO == null) {
            throw new AppException("Partner data cannot be null.");
        }
        if (partnerDTO.name() == null || partnerDTO.name().trim().isEmpty()) {
            throw new AppException("Partner name cannot be null or empty.");
        }
        if (partnerDTO.catalogFile() == null || partnerDTO.catalogFile().trim().isEmpty()) {
            throw new AppException("Catalog file cannot be null or empty.");
        }
        if (partnerDTO.description() == null || partnerDTO.description().trim().isEmpty()) {
            throw new AppException("Description cannot be null or empty.");
        }
    }
}

