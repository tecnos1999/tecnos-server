package com.example.tecnosserver.subcategory.service;

import com.example.tecnosserver.exceptions.exception.NotFoundException;
import com.example.tecnosserver.subcategory.model.SubCategory;
import com.example.tecnosserver.subcategory.repo.SubCategoryRepo;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
public class SubCategoryQueryServiceImpl implements SubCategoryQueryService{

    private final SubCategoryRepo subCategoryRepo;

    public SubCategoryQueryServiceImpl(SubCategoryRepo subCategoryRepo) {
        this.subCategoryRepo = subCategoryRepo;
    }

    @Override
    public Optional<SubCategory> findSubCategoryByName(String name) {
        Optional<SubCategory> subCategory = subCategoryRepo.findSubCategoryByName(name);
        if (subCategory.isEmpty()) {
            throw new NotFoundException("Sub category with name " + name + " not found");
        }
        return subCategory;
    }
}
