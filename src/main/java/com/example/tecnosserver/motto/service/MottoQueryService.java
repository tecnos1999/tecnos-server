package com.example.tecnosserver.motto.service;



import com.example.tecnosserver.motto.dto.MottoDTO;

import java.util.List;
import java.util.Optional;

public interface MottoQueryService {

    Optional<MottoDTO> getMottoByCode(String code);

    Optional<List<MottoDTO>> getAllMottos();
}

