package com.example.tecnosserver.tags.dto;


import lombok.Builder;

@Builder
public record TagDTO(
        String name,
        String color
) {}
