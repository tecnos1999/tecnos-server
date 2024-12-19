package com.example.tecnosserver.itemcategory.mapper;


import com.example.tecnosserver.itemcategory.dto.ItemCategoryDTO;
import com.example.tecnosserver.itemcategory.model.ItemCategory;
import org.springframework.stereotype.Component;


import com.example.tecnosserver.products.model.Product;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ItemCategoryMapper {

    public ItemCategoryDTO toDTO(ItemCategory itemCategory) {
        if (itemCategory == null) {
            return null;
        }

        List<String> productNames = itemCategory.getProducts().stream()
                .map(Product::getName)
                .collect(Collectors.toList());

        return ItemCategoryDTO.builder()
                .name(itemCategory.getName())
                .products(productNames)
                .createdAt(itemCategory.getCreatedAt())
                .updatedAt(itemCategory.getUpdatedAt())
                .subcategoryName(itemCategory.getSubCategory().getName())
                .categoryName(itemCategory.getCategory().getName())
                .build();
    }
}


