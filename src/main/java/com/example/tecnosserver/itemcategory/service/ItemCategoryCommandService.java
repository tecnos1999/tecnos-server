package com.example.tecnosserver.itemcategory.service;


public interface ItemCategoryCommandService {

    void createItemCategory(String name, String subCategory);

    void updateItemCategory(String name, String updatedName);

    void deleteItemCategory(String name);
}

