package com.example.tecnosserver.webinar.service;

import com.example.tecnosserver.exceptions.exception.AppException;
import com.example.tecnosserver.exceptions.exception.NotFoundException;
import com.example.tecnosserver.webinar.dto.WebinarDTO;
import com.example.tecnosserver.webinar.mapper.WebinarMapper;
import com.example.tecnosserver.webinar.model.Webinar;
import com.example.tecnosserver.webinar.repo.WebinarRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WebinarQueryServiceImpl implements WebinarQueryService{

    private final WebinarRepo webinarRepo;
    private final WebinarMapper webinarMapper;

    @Override
    public Optional<WebinarDTO> findWebinarByCode(String webCode) {
        if (webCode == null || webCode.trim().isEmpty()) {
            throw new AppException("Webinar code cannot be null or empty.");
        }

        return webinarRepo.findWebinarByWebCode(webCode)
                .map(webinarMapper::toDTO)
                .or(() -> {
                    throw new NotFoundException("Webinar with code " + webCode + " not found.");
                });
    }


    @Override
    public Optional<List<WebinarDTO>> findAllWebinars() {
        List<Webinar> webinars = webinarRepo.findAll();
        if (webinars.isEmpty()) {
            throw new NotFoundException("No webinars found.");
        }

        List<WebinarDTO> webinarDTOs = webinars.stream()
                .map(webinarMapper::toDTO)
                .collect(Collectors.toList());

        return Optional.of(webinarDTOs);
    }
}
