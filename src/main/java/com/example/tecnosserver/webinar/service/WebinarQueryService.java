package com.example.tecnosserver.webinar.service;
import com.example.tecnosserver.webinar.dto.WebinarDTO;

import java.util.List;
import java.util.Optional;

public interface WebinarQueryService {

    Optional<WebinarDTO> findWebinarByCode(String webCode);

    Optional<List<WebinarDTO>> findAllWebinars();
}
