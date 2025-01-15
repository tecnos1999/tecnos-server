package com.example.tecnosserver.infocard.service;

import com.example.tecnosserver.infocard.dto.InfoCardDTO;

public interface InfoCardCommandService {

    InfoCardDTO addInfoCard(InfoCardDTO infoCardDTO);

    void updateInfoCard(String code, InfoCardDTO infoCardDTO);

    void deleteInfoCard(String code);
}
