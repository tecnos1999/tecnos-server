package com.example.tecnosserver.subcategory.service;

import com.example.tecnosserver.subcategory.dto.SubCategoryDTO;
import com.example.tecnosserver.subcategory.model.SubCategory;

import java.util.List;
import java.util.Optional;

public interface SubCategoryQueryService {

        Optional<List<SubCategoryDTO>> findAllSubCategories();
}
