package com.example.tecnosserver.partners.service;

import com.example.tecnosserver.partners.dto.PartnerDTO;
import org.springframework.web.multipart.MultipartFile;

public interface PartnerCommandService {
    void addPartner(PartnerDTO partnerDTO, MultipartFile image, MultipartFile catalogFile);

    void deletePartner(String name);

    void updatePartner(String name, PartnerDTO partnerDTO, MultipartFile image, MultipartFile catalogFile);

}

