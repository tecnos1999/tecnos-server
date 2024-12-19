package com.example.tecnosserver.subcategory.mapper;


import com.example.tecnosserver.subcategory.dto.SubCategoryDTO;
import com.example.tecnosserver.subcategory.model.SubCategory;
import org.springframework.stereotype.Component;

@Component
public class SubCategoryMapper {

    public SubCategoryDTO toDTO(SubCategory subCategory) {
        if (subCategory == null) {
            return null;
        }

        return SubCategoryDTO.builder()
                .name(subCategory.getName())
                .createdAt(subCategory.getCreatedAt())
                .updatedAt(subCategory.getUpdatedAt())
                .categoryName(subCategory.getCategory().getName())
                .build();
    }
}

