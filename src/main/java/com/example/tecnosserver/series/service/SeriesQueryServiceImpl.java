package com.example.tecnosserver.series.service;

import com.example.tecnosserver.exceptions.exception.AppException;
import com.example.tecnosserver.exceptions.exception.NotFoundException;
import com.example.tecnosserver.series.dto.SeriesDTO;
import com.example.tecnosserver.series.mapper.SeriesMapper;
import com.example.tecnosserver.series.model.Series;
import com.example.tecnosserver.series.repo.SeriesRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SeriesQueryServiceImpl implements SeriesQueryService {

    private final SeriesRepo seriesRepo;
    private final SeriesMapper seriesMapper;

    @Override
    public Optional<SeriesDTO> findSeriesByCode(String code) {
        if (code == null || code.trim().isEmpty()) {
            throw new AppException("Series code cannot be null or empty.");
        }

        return seriesRepo.findByCode(code)
                .map(seriesMapper::toDTO)
                .or(() -> {
                    throw new NotFoundException("Series with code " + code + " not found.");
                });
    }

    @Override
    public Optional<List<SeriesDTO>> findAllSeries() {
        List<Series> seriesList = seriesRepo.findAll();
        if (seriesList.isEmpty()) {
            throw new NotFoundException("No series found.");
        }

        List<SeriesDTO> seriesDTOs = seriesList.stream()
                .map(seriesMapper::toDTO)
                .collect(Collectors.toList());

        return Optional.of(seriesDTOs);
    }
}
