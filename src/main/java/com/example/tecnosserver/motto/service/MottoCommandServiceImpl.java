package com.example.tecnosserver.motto.service;


import com.example.tecnosserver.motto.dto.MottoDTO;
import com.example.tecnosserver.motto.mapper.MottoMapper;
import com.example.tecnosserver.motto.model.Motto;
import com.example.tecnosserver.motto.repo.MottoRepo;
import com.example.tecnosserver.exceptions.exception.AppException;
import com.example.tecnosserver.exceptions.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class MottoCommandServiceImpl implements MottoCommandService {

    private final MottoRepo mottoRepo;
    private final MottoMapper mottoMapper;

    @Override
    public void addMotto(MottoDTO mottoDTO) {
        try {
            Motto motto = mottoMapper.toEntity(mottoDTO);
            mottoRepo.save(motto);
            log.info("Motto added successfully: {}", motto);
        } catch (Exception e) {
            log.error("Error adding motto: {}", e.getMessage());
            throw new AppException("An error occurred while adding the motto.", e);
        }
    }

    @Override
    public void updateMotto(String code, MottoDTO mottoDTO) {
        try {
            Motto motto = mottoRepo.findByCode(code)
                    .orElseThrow(() -> new NotFoundException("Motto with code '" + code + "' not found."));

            motto.setContent(mottoDTO.content());
            mottoRepo.save(motto);
            log.info("Motto updated successfully: {}", motto);
        } catch (NotFoundException e) {
            log.error("Error updating motto: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("Error updating motto: {}", e.getMessage());
            throw new AppException("An error occurred while updating the motto.", e);
        }
    }

    @Override
    public void deleteMotto(String code) {
        try {
            Motto motto = mottoRepo.findByCode(code)
                    .orElseThrow(() -> new NotFoundException("Motto with code '" + code + "' not found."));
            mottoRepo.delete(motto);
            log.info("Motto deleted successfully: {}", motto);
        } catch (NotFoundException e) {
            log.error("Error deleting motto: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("Error deleting motto: {}", e.getMessage());
            throw new AppException("An error occurred while deleting the motto.", e);
        }
    }
}

