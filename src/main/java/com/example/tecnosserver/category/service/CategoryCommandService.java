package com.example.tecnosserver.category.service;

public interface CategoryCommandService {

    void createCategory(String name , String mainSection);

    void updateCategory(String name,String updatedName , String updatedMainSection);

    void deleteCategory(String name);
}
