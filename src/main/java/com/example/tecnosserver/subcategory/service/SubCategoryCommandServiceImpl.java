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
    public void createSubCategory(String name, String category) {
        Optional<Category> categoryOptional = categoryRepo.findCategoryByName(category);
        if (categoryOptional.isEmpty()) {
            throw new NotFoundException("Category with name '" + category + "' not found");
        }

        boolean subCategoryExists = subCategoryRepo.existsByNameAndCategory(name, categoryOptional.get());
        if (subCategoryExists) {
            throw new AlreadyExistsException("Subcategory with name '" + name + "' already exists in category '" + category + "'");
        }

        SubCategory newSubCategory = new SubCategory();
        newSubCategory.setName(name);
        newSubCategory.setCategory(categoryOptional.get());

        subCategoryRepo.saveAndFlush(newSubCategory);
    }



    @Override
    public void updateSubCategory(String name, String updatedName, String category) {
        Category categoryEntity = categoryRepo.findCategoryByName(category)
                .orElseThrow(() -> new NotFoundException("Category with name '" + category + "' not found"));

        SubCategory subCategoryToUpdate = subCategoryRepo.findByNameAndCategory(name, categoryEntity)
                .orElseThrow(() -> new NotFoundException("Subcategory with name '" + name + "' not found in category '" + category + "'"));

        boolean subCategoryExists = subCategoryRepo.existsByNameAndCategory(updatedName, categoryEntity);
        if (subCategoryExists && !subCategoryToUpdate.getName().equals(updatedName)) {
            throw new AlreadyExistsException("Subcategory with name '" + updatedName + "' already exists in category '" + category + "'");
        }

        subCategoryToUpdate.setName(updatedName);

        subCategoryRepo.saveAndFlush(subCategoryToUpdate);
    }



    @Override
    public void deleteSubCategory(String name, String category) {
        Category categoryEntity = categoryRepo.findCategoryByName(category)
                .orElseThrow(() -> new NotFoundException("Category with name '" + category + "' not found"));

        SubCategory subCategoryToDelete = subCategoryRepo.findByNameAndCategory(name, categoryEntity)
                .orElseThrow(() -> new NotFoundException("Subcategory with name '" + name + "' not found in category '" + category + "'"));

        subCategoryRepo.delete(subCategoryToDelete);
    }

}
