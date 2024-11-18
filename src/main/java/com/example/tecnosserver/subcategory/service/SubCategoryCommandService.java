package com.example.tecnosserver.subcategory.service;


public interface SubCategoryCommandService {

    void createSubCategory(String name,String category);

    void updateSubCategory(String name, String updatedName, String category);

    void deleteSubCategory(String name , String category);
}
