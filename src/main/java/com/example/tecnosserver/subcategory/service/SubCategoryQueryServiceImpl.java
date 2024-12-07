package com.example.tecnosserver.subcategory.service;

import com.example.tecnosserver.exceptions.exception.NotFoundException;
import com.example.tecnosserver.subcategory.model.SubCategory;
import com.example.tecnosserver.subcategory.repo.SubCategoryRepo;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
public class SubCategoryQueryServiceImpl implements SubCategoryQueryService{

    private final SubCategoryRepo subCategoryRepo;

    public SubCategoryQueryServiceImpl(SubCategoryRepo subCategoryRepo) {
        this.subCategoryRepo = subCategoryRepo;
    }


    @Override
    public Optional<List<SubCategory>> findAllSubCategories() {
        List<SubCategory> subCategories = subCategoryRepo.findAll();
        if (subCategories.isEmpty()) {
            throw new NotFoundException("No sub categories found");
        }
        return Optional.of(subCategories);
    }
}
