package com.example.tecnosserver.series.service;


import com.example.tecnosserver.series.dto.SeriesDTO;
import org.springframework.web.multipart.MultipartFile;

public interface SeriesCommandService {
    void addSeries(SeriesDTO seriesDTO, MultipartFile image);

    void updateSeries(String code, SeriesDTO seriesDTO, MultipartFile image);

    void deleteSeries(String code);
}

