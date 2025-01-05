package com.example.tecnosserver.series.dto;


import java.util.List;

public record SeriesDTO(
        String code,
        String name,
        String description,
        String imageUrl,
        List<String> blogCodes
) {}

