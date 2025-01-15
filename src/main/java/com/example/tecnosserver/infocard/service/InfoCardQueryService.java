package com.example.tecnosserver.infocard.service;


import com.example.tecnosserver.infocard.dto.InfoCardDTO;

import java.util.List;

public interface InfoCardQueryService {

    InfoCardDTO getInfoCardByCode(String code);

    List<InfoCardDTO> getAllInfoCards();

}

