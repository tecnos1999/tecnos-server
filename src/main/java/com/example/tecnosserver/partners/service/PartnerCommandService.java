package com.example.tecnosserver.partners.service;
import com.example.tecnosserver.partners.dto.PartnerDTO;

public interface PartnerCommandService {
    void addPartner(PartnerDTO partnerDTO);
    void deletePartner(String name);
}

