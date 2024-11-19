package com.example.tecnosserver.itemcategory.service;


import org.springframework.transaction.annotation.Transactional;

public interface ItemCategoryCommandService {

    void createItemCategory(String name, String subCategory, String category);

    void updateItemCategory(
            String name,
            String updatedName,
            String subCategoryName,
            String categoryName,
            String updatedSubCategoryName,
            String updatedCategoryName);

    void deleteItemCategory(String name, String subCategory, String category);
}

