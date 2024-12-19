package com.example.tecnosserver.itemcategory.dto;


import lombok.Builder;
import lombok.Data;
import org.springframework.cglib.core.Local;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Data
@Builder
public class ItemCategoryDTO {
    private String name;
    private List<String> products;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String subcategoryName;
    private String categoryName;
}


