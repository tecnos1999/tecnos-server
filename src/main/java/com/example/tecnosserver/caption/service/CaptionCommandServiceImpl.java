package com.example.tecnosserver.caption.service;

import com.example.tecnosserver.caption.dto.CaptionDTO;
import com.example.tecnosserver.caption.mapper.CaptionMapper;
import com.example.tecnosserver.caption.model.Caption;
import com.example.tecnosserver.caption.repo.CaptionRepo;
import com.example.tecnosserver.exceptions.exception.AppException;
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
public class CaptionCommandServiceImpl implements CaptionCommandService {

    private final CaptionRepo captionRepo;
    private final CaptionMapper captionMapper;
    private final CloudAdapter cloudAdapter;

    @Override
    public void addCaption(CaptionDTO captionDTO, MultipartFile file) {
        String fileUrl = null;

        if (file != null && !file.isEmpty()) {
            fileUrl = uploadFile(file, "Failed to upload file: ");
        }

        Caption caption = captionMapper.fromDTO(captionDTO);
        caption.setPhotoUrl(fileUrl);
        caption.setActive(false);

        captionRepo.save(caption);
        log.info("Caption created successfully: {}", caption);
    }

    @Override
    public void updateCaption(String code, CaptionDTO captionDTO, MultipartFile file) {
        Caption caption = captionRepo.findByCode(code)
                .orElseThrow(() -> new NotFoundException("Caption not found with code: " + code));

        if (file != null && !file.isEmpty()) {
            safeDeleteFile(caption.getPhotoUrl());
            String fileUrl = uploadFile(file, "Failed to upload new file: ");
            caption.setPhotoUrl(fileUrl);
        }
        caption.setTitle(captionDTO.title());
        caption.setText(captionDTO.text());
        caption.setPosition(captionDTO.position());
        caption.setActive(captionDTO.active());

        captionRepo.save(caption);
        log.info("Caption updated successfully: {}", caption);
    }

    @Override
    public void deleteCaption(String code) {
        Caption caption = captionRepo.findByCode(code)
                .orElseThrow(() -> new NotFoundException("Caption not found with code: " + code));

        safeDeleteFile(caption.getPhotoUrl());
        captionRepo.delete(caption);
        log.info("Caption deleted successfully: {}", code);
    }

    private String uploadFile(MultipartFile file, String errorMessage) {
        try {
            return cloudAdapter.uploadFile(file);
        } catch (Exception e) {
            log.error("{} {}", errorMessage, e.getMessage());
            throw new AppException(errorMessage + e.getMessage());
        }
    }

    private void safeDeleteFile(String fileUrl) {
        if (fileUrl != null && !fileUrl.isBlank()) {
            try {
                cloudAdapter.deleteFile(fileUrl);
                log.info("Deleted file from cloud: {}", fileUrl);
            } catch (Exception e) {
                log.error("Failed to delete associated file: {}", e.getMessage());
                throw new AppException("Failed to delete associated file: " + e.getMessage(), e);
            }
        }
    }
}
