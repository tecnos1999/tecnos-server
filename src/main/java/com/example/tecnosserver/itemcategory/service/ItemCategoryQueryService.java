package com.example.tecnosserver.itemcategory.service;

import com.example.tecnosserver.itemcategory.dto.ItemCategoryDTO;
import com.example.tecnosserver.itemcategory.model.ItemCategory;

import java.util.List;
import java.util.Optional;

public interface ItemCategoryQueryService {




    Optional<List<ItemCategoryDTO>> findAllItemCategories(String subCategoryName, String categoryName);
}