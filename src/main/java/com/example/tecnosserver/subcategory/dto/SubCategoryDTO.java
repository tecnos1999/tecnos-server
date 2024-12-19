package com.example.tecnosserver.subcategory.dto;


import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class SubCategoryDTO {
    private String name;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String categoryName;
}
