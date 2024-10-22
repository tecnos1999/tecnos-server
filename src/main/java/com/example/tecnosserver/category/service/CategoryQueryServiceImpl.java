package com.example.tecnosserver.category.service;

import com.example.tecnosserver.category.model.Category;
import com.example.tecnosserver.category.repo.CategoryRepo;
import com.example.tecnosserver.exceptions.exception.NotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
public class CategoryQueryServiceImpl implements CategoryQueryService{

    private final CategoryRepo categoryRepo;

    public CategoryQueryServiceImpl(CategoryRepo categoryRepo) {
        this.categoryRepo = categoryRepo;
    }

    @Override
    public Optional<Category> findCategoryByName(String name) {
        Optional<Category> category = categoryRepo.findCategoryByName(name);
        if (category.isEmpty()) {
            throw new NotFoundException("Category with name " + name + " not found");
        }
        return category;
    }

    @Override
    public Optional<List<Category>> findAllCategories() {
        List<Category> categories = categoryRepo.findAll();
        if (categories.isEmpty()) {
            throw new NotFoundException("No categories found");
        }
        return Optional.of(categories);
    }
}
