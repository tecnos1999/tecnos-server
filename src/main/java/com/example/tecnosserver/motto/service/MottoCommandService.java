package com.example.tecnosserver.motto.service;

import com.example.tecnosserver.motto.dto.MottoDTO;

public interface MottoCommandService {

    void addMotto(MottoDTO mottoDTO);

    void updateMotto(String code, MottoDTO mottoDTO);

    void deleteMotto(String code);
}
