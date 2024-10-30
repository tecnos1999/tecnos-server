package com.example.tecnosserver.itemcategory.service;

import com.example.tecnosserver.itemcategory.model.ItemCategory;

import java.util.List;
import java.util.Optional;

public interface ItemCategoryQueryService {

    Optional<ItemCategory> findItemCategoryByName(String name);

    Optional<List<ItemCategory>> findAllItemCategories();
}