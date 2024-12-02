package com.example.tecnosserver.partners.service;

import com.example.tecnosserver.partners.dto.PartnerDTO;

import java.util.List;
import java.util.Optional;

public interface PartnerQueryService {

    Optional<PartnerDTO> findPartnerByName(String name);

    Optional<List<PartnerDTO>> findAllPartners();

}
