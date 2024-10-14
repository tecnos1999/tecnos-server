package com.example.tecnosserver.subcategory.service;

import com.example.tecnosserver.category.model.Category;
import com.example.tecnosserver.category.repo.CategoryRepo;
import com.example.tecnosserver.exceptions.exception.AlreadyExistsException;
import com.example.tecnosserver.exceptions.exception.NotFoundException;
import com.example.tecnosserver.subcategory.model.SubCategory;
import com.example.tecnosserver.subcategory.repo.SubCategoryRepo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class SubCategoryCommandServiceImpl implements SubCategoryCommandService{

    private final SubCategoryRepo subCategoryRepo;

    private final CategoryRepo categoryRepo;

    public SubCategoryCommandServiceImpl(SubCategoryRepo subCategoryRepo, CategoryRepo categoryRepo) {
        this.subCategoryRepo = subCategoryRepo;
        this.categoryRepo = categoryRepo;
    }

    @Override
    public void createSubCategory(String name) {
        Optional<SubCategory> subCategory = subCategoryRepo.findSubCategoryByName(name);
        if (subCategory.isPresent()) {
            throw new AlreadyExistsException("Subcategory with name " + name + " already exists");
        }

        SubCategory newSubCategory = new SubCategory();
        newSubCategory.setName(name);

        subCategoryRepo.saveAndFlush(newSubCategory);
    }


    @Override
    public void updateSubCategory(String name, String updatedName) {
        SubCategory subCategoryToUpdate = subCategoryRepo.findSubCategoryByName(name)
                .orElseThrow(() -> new NotFoundException("Subcategory with name '" + name + "' not found"));

        subCategoryRepo.findSubCategoryByName(updatedName).ifPresent(existingSubCategory -> {
            if (!existingSubCategory.getId().equals(subCategoryToUpdate.getId())) {
                throw new AlreadyExistsException("Subcategory with name '" + updatedName + "' already exists");
            }
        });

        subCategoryToUpdate.setName(updatedName);

        subCategoryRepo.saveAndFlush(subCategoryToUpdate);
    }


    @Override
    public void deleteSubCategory(String name) {

        Optional<SubCategory> subCategoryToDelete = subCategoryRepo.findSubCategoryByName(name);
        if(subCategoryToDelete.isEmpty()) {
            throw new NotFoundException("Subcategory with name '" + name + "' not found");
        }
        subCategoryRepo.delete(subCategoryToDelete.get());
    }
}
