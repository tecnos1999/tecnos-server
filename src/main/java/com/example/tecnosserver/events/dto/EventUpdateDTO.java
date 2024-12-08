package com.example.tecnosserver.events.dto;


import lombok.Builder;

@Builder
public record EventUpdateDTO(
        String title,
        String description,
        String externalLink,
        String newImageUrl
) {}

