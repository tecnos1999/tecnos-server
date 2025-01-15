package com.example.tecnosserver.infocard.service;

import com.example.tecnosserver.exceptions.exception.NotFoundException;
import com.example.tecnosserver.infocard.dto.InfoCardDTO;
import com.example.tecnosserver.infocard.mapper.InfoCardMapper;
import com.example.tecnosserver.infocard.model.InfoCard;
import com.example.tecnosserver.infocard.repo.InfoCardRepo;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class InfoCardQueryServiceImpl implements InfoCardQueryService {

    private final InfoCardRepo infoCardRepository;
    private final InfoCardMapper infoCardMapper;

    @Override
    public InfoCardDTO getInfoCardByCode(String code) {

        Optional<InfoCard> infoCard = infoCardRepository.findByCode(code);
        if (infoCard.isEmpty()) {
            log.error("InfoCard with code '{}' not found.", code);
            throw new NotFoundException("InfoCard with code '" + code + "' not found.");
        }
        return infoCardMapper.toDTO(infoCard.get());
    }

    @Override
    public List<InfoCardDTO> getAllInfoCards() {
        log.info("Fetching all InfoCards from database.");
        List<InfoCardDTO> infoCards = infoCardRepository.findAll().stream()
                .map(infoCardMapper::toDTO)
                .collect(Collectors.toList());
        if (infoCards.isEmpty()) {
            log.warn("No InfoCards found in the database.");
            throw new NotFoundException("No InfoCards found.");
        }
        log.info("Successfully fetched {} InfoCards.", infoCards.size());
        return infoCards;
    }
}
