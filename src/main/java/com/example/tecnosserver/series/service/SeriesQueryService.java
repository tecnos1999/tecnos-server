package com.example.tecnosserver.series.service;


import com.example.tecnosserver.series.dto.SeriesDTO;

import java.util.List;
import java.util.Optional;

public interface SeriesQueryService {
    Optional<SeriesDTO> findSeriesByCode(String code);

    Optional<SeriesDTO> findSeriesByName(String name);
    Optional<List<SeriesDTO>> findAllSeries();
}
