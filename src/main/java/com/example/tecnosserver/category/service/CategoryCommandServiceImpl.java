package com.example.tecnosserver.category.service;

import com.example.tecnosserver.category.model.Category;
import com.example.tecnosserver.category.repo.CategoryRepo;
import com.example.tecnosserver.exceptions.exception.AlreadyExistsException;
import com.example.tecnosserver.exceptions.exception.NotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
public class CategoryCommandServiceImpl implements CategoryCommandService{
    private final CategoryRepo categoryRepo;

    public CategoryCommandServiceImpl(CategoryRepo categoryRepo) {
        this.categoryRepo = categoryRepo;
    }

    @Override
    public void createCategory(String name) {
        Optional<Category> category = categoryRepo.findCategoryByName(name);
        if(category.isPresent()) {
            throw new AlreadyExistsException("Category with name " + name + " already exists");
        }
        Category newCategory = new Category();
        newCategory.setName(name);
        categoryRepo.saveAndFlush(newCategory);
    }

    @Override
    public void updateCategory(String name, String updatedName) {
        Category categoryToUpdate = categoryRepo.findCategoryByName(name)
                .orElseThrow(() -> new NotFoundException("Category with name '" + name + "' not found"));

        categoryRepo.findCategoryByName(updatedName).ifPresent(existingCategory -> {
            if (!existingCategory.getId().equals(categoryToUpdate.getId())) {
                throw new AlreadyExistsException("Category with name '" + updatedName + "' already exists");
            }
        });

        categoryToUpdate.setName(updatedName);

        categoryRepo.saveAndFlush(categoryToUpdate);
    }

    @Override
    public void deleteCategory(String name) {
        Category categoryToDelete = categoryRepo.findCategoryByName(name)
                .orElseThrow(() -> new NotFoundException("Category with name '" + name + "' not found"));

        categoryRepo.delete(categoryToDelete);
    }

}
