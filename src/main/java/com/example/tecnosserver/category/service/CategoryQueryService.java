package com.example.tecnosserver.category.service;

import com.example.tecnosserver.category.model.Category;

import java.util.List;
import java.util.Optional;

public interface CategoryQueryService {

    Optional<Category> findCategoryByName(String name);

    Optional<List<Category>> findAllCategories();
}
