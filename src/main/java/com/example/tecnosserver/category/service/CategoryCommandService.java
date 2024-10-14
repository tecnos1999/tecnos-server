package com.example.tecnosserver.category.service;

public interface CategoryCommandService {

    void createCategory(String name);

    void updateCategory(String name,String updatedName);

    void deleteCategory(String name);
}
