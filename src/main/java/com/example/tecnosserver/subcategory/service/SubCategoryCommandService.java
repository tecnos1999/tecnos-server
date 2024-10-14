package com.example.tecnosserver.subcategory.service;


public interface SubCategoryCommandService {

    void createSubCategory(String name);

    void updateSubCategory(String name, String updatedName);

    void deleteSubCategory(String name);
}
