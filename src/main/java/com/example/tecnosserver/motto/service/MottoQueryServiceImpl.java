package com.example.tecnosserver.motto.service;

import com.example.tecnosserver.motto.dto.MottoDTO;
import com.example.tecnosserver.motto.mapper.MottoMapper;
import com.example.tecnosserver.motto.repo.MottoRepo;
import com.example.tecnosserver.exceptions.exception.AppException;
import com.example.tecnosserver.exceptions.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class MottoQueryServiceImpl implements MottoQueryService {

    private final MottoRepo mottoRepo;
    private final MottoMapper mottoMapper;

    @Override
    public Optional<MottoDTO> getMottoByCode(String code) {
        try {
            return mottoRepo.findByCode(code)
                    .map(mottoMapper::toDTO)
                    .or(() -> {
                        log.warn("Motto with code '{}' not found.", code);
                        throw new NotFoundException("Motto with code '" + code + "' not found.");
                    });
        } catch (Exception e) {
            log.error("Error fetching motto by code: {}", e.getMessage());
            throw new AppException("An error occurred while fetching the motto.", e);
        }
    }

    @Override
    public Optional<List<MottoDTO>> getAllMottos() {
        try {
            List<MottoDTO> mottos = mottoRepo.findAll().stream()
                    .map(mottoMapper::toDTO)
                    .collect(Collectors.toList());
            return mottos.isEmpty() ? Optional.empty() : Optional.of(mottos);
        } catch (Exception e) {
            log.error("Error fetching all mottos: {}", e.getMessage());
            throw new AppException("An error occurred while fetching mottos.", e);
        }
    }

}
