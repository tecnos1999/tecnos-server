package com.example.tecnosserver.itemcategory.service;


public interface ItemCategoryCommandService {

    void createItemCategory(String name, String subCategory , String category);

    void updateItemCategory(String name, String updatedName , String subCategory , String category);

    void deleteItemCategory(String name , String subCategory , String category);
}

