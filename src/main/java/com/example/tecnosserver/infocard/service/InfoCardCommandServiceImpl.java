package com.example.tecnosserver.infocard.service;

import com.example.tecnosserver.exceptions.exception.AlreadyExistsException;
import com.example.tecnosserver.infocard.dto.InfoCardDTO;
import com.example.tecnosserver.infocard.mapper.InfoCardMapper;
import com.example.tecnosserver.infocard.model.InfoCard;
import com.example.tecnosserver.infocard.repo.InfoCardRepo;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class InfoCardCommandServiceImpl implements InfoCardCommandService {
    private final InfoCardRepo infoCardRepository;
    private final InfoCardMapper infoCardMapper;

    @Override
    public InfoCardDTO addInfoCard(InfoCardDTO infoCardDTO) {
        log.info("Attempting to add InfoCard with code: {}", infoCardDTO.code());

        Optional<InfoCard> existingInfoCard = infoCardRepository.findByCode(infoCardDTO.code());
        if (existingInfoCard.isPresent()) {
            log.error("InfoCard with code '{}' already exists.", infoCardDTO.code());
            throw new AlreadyExistsException("InfoCard with code '" + infoCardDTO.code() + "' already exists.");
        }

        InfoCard infoCardFromDTO = infoCardMapper.fromDTO(infoCardDTO);
        InfoCard savedInfoCard = infoCardRepository.save(infoCardFromDTO);

        InfoCardDTO savedInfoCardDTO = infoCardMapper.toDTO(savedInfoCard);
        log.info("InfoCard with code '{}' has been added.", savedInfoCardDTO.code());

        return savedInfoCardDTO;
    }


    @Override
    public void updateInfoCard(String code, InfoCardDTO infoCardDTO) {
        log.info("Attempting to update InfoCard with code: {}", code);
        Optional<InfoCard> infoCard = infoCardRepository.findByCode(code);
        if (infoCard.isEmpty()) {
            log.error("InfoCard with code '{}' not found.", code);
            throw new AlreadyExistsException("InfoCard with code '" + code + "' not found.");
        }

        log.info("Updating fields for InfoCard with code: {}", code);
        infoCard.get().setTitle(infoCardDTO.title());
        infoCard.get().setDescription(infoCardDTO.description());
        infoCard.get().setFeatures(infoCardDTO.features());
        infoCardRepository.save(infoCard.get());
        log.info("Successfully updated InfoCard with code: {}", code);
    }

    @Override
    public void deleteInfoCard(String code) {
        log.info("Attempting to delete InfoCard with code: {}", code);
        Optional<InfoCard> infoCard = infoCardRepository.findByCode(code);
        if (infoCard.isEmpty()) {
            log.error("InfoCard with code '{}' not found.", code);
            throw new AlreadyExistsException("InfoCard with code '" + code + "' not found.");
        }
        infoCardRepository.delete(infoCard.get());
        log.info("Successfully deleted InfoCard with code: {}", code);
    }
}
