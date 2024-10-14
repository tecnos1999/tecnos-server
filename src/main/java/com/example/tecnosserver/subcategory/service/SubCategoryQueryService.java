package com.example.tecnosserver.subcategory.service;

import com.example.tecnosserver.subcategory.model.SubCategory;

import java.util.Optional;

public interface SubCategoryQueryService {

        Optional<SubCategory> findSubCategoryByName(String name);
}
