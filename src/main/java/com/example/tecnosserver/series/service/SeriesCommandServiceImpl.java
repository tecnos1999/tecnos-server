package com.example.tecnosserver.series.service;

import com.example.tecnosserver.series.dto.SeriesDTO;
import com.example.tecnosserver.series.mapper.SeriesMapper;
import com.example.tecnosserver.series.model.Series;
import com.example.tecnosserver.series.repo.SeriesRepo;
import com.example.tecnosserver.exceptions.exception.NotFoundException;
import com.example.tecnosserver.intercom.CloudAdapter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class SeriesCommandServiceImpl implements SeriesCommandService {

    private final SeriesRepo seriesRepo;
    private final SeriesMapper seriesMapper;
    private final CloudAdapter cloudAdapter;

    @Override
    public void addSeries(SeriesDTO seriesDTO, MultipartFile image) {
        log.info("Starting to add a new series with name: {}", seriesDTO.name());

        Series series = seriesMapper.fromDTO(seriesDTO);

        if (image != null && !image.isEmpty()) {
            String imageUrl = uploadFile(image);
            series.setImageUrl(imageUrl);
            log.info("Image uploaded successfully for series: {}", seriesDTO.name());
        } else {
            log.warn("No image provided for series: {}", seriesDTO.name());
        }

        seriesRepo.save(series);
        log.info("Series '{}' added successfully with code: {}", seriesDTO.name(), series.getCode());
    }

    @Override
    public void updateSeries(String code, SeriesDTO seriesDTO, MultipartFile image) {
        log.info("Starting to update series with code: {}", code);

        Series series = seriesRepo.findByCode(code)
                .orElseThrow(() -> {
                    log.error("Series with code '{}' not found.", code);
                    return new NotFoundException("Series with code '" + code + "' not found.");
                });

        series.setName(seriesDTO.name());
        series.setDescription(seriesDTO.description());

        if (image != null && !image.isEmpty()) {
            log.info("Uploading new image for series: {}", code);
            String imageUrl = uploadFile(image);
            series.setImageUrl(imageUrl);
            log.info("Image updated successfully for series: {}", code);
        } else {
            log.info("No new image provided for series: {}", code);
        }

        seriesRepo.save(series);
        log.info("Series '{}' updated successfully.", series.getName());
    }

    @Override
    public void deleteSeries(String code) {
        log.info("Starting to delete series with code: {}", code);

        Series series = seriesRepo.findByCode(code)
                .orElseThrow(() -> {
                    log.error("Series with code '{}' not found.", code);
                    return new NotFoundException("Series with code '" + code + "' not found.");
                });

        seriesRepo.delete(series);
        log.info("Series with code '{}' deleted successfully.", code);
    }

    private String uploadFile(MultipartFile file) {
        try {
            log.info("Uploading file: {}", file.getOriginalFilename());
            return cloudAdapter.uploadFile(file);
        } catch (Exception e) {
            log.error("Failed to upload image: ", e);
            throw new RuntimeException("Failed to upload image: " + e.getMessage());
        }
    }
}
