package com.example.tecnosserver.category.service;

import com.example.tecnosserver.category.model.Category;
import com.example.tecnosserver.category.repo.CategoryRepo;
import com.example.tecnosserver.exceptions.exception.NotFoundException;
import com.example.tecnosserver.utils.AdvancedStructuredLogger;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryQueryServiceImpl implements CategoryQueryService {

    private final CategoryRepo categoryRepo;
    private final AdvancedStructuredLogger advancedStructuredLogger;

    public CategoryQueryServiceImpl(CategoryRepo categoryRepo, AdvancedStructuredLogger advancedStructuredLogger) {
        this.categoryRepo = categoryRepo;
        this.advancedStructuredLogger = advancedStructuredLogger;
    }

    @Override
    public Optional<Category> findCategoryByName(String name) {
        advancedStructuredLogger.logBuilder()
                .withEventType("CATEGORY_QUERY_BY_NAME_START")
                .withMessage("Searching for category by name")
                .withField("categoryName", name)
                .withLevel("INFO")
                .log();

        Optional<Category> category = categoryRepo.findCategoryByName(name);

        if (category.isEmpty()) {
            advancedStructuredLogger.logBuilder()
                    .withEventType("CATEGORY_QUERY_BY_NAME_NOT_FOUND")
                    .withMessage("Category not found")
                    .withField("categoryName", name)
                    .withLevel("WARN")
                    .log();
            throw new NotFoundException("Category with name " + name + " not found");
        }

        advancedStructuredLogger.logBuilder()
                .withEventType("CATEGORY_QUERY_BY_NAME_SUCCESS")
                .withMessage("Category found successfully")
                .withField("categoryName", name)
                .withField("categoryId", category.get().getId())
                .withLevel("INFO")
                .log();

        return category;
    }

    @Override
    public Optional<List<Category>> findAllCategories() {
        advancedStructuredLogger.logBuilder()
                .withEventType("CATEGORY_QUERY_ALL_START")
                .withMessage("Fetching all categories")
                .withLevel("INFO")
                .log();

        List<Category> categories = categoryRepo.findAll();

        if (categories.isEmpty()) {
            advancedStructuredLogger.logBuilder()
                    .withEventType("CATEGORY_QUERY_ALL_NOT_FOUND")
                    .withMessage("No categories found")
                    .withLevel("WARN")
                    .log();
            throw new NotFoundException("No categories found");
        }

        advancedStructuredLogger.logBuilder()
                .withEventType("CATEGORY_QUERY_ALL_SUCCESS")
                .withMessage("Successfully fetched all categories")
                .withField("categoryCount", categories.size())
                .withLevel("INFO")
                .log();

        return Optional.of(categories);
    }
}
