package com.example.tecnosserver.motto.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

public record MottoDTO(
        String code,

        @NotBlank(message = "Text cannot be blank.")
        @Size(max = 255, message = "Text must not exceed 255 characters.")
        String content,

        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
