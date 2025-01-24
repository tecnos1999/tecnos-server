package com.example.tecnosserver.category.service;

import com.example.tecnosserver.category.model.Category;
import com.example.tecnosserver.category.repo.CategoryRepo;
import com.example.tecnosserver.exceptions.exception.AlreadyExistsException;
import com.example.tecnosserver.exceptions.exception.NotFoundException;
import com.example.tecnosserver.utils.AdvancedStructuredLogger;
import com.example.tecnosserver.utils.MainSection;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class CategoryCommandServiceImpl implements CategoryCommandService {

    private final CategoryRepo categoryRepo;
    private final AdvancedStructuredLogger advancedStructuredLogger;

    public CategoryCommandServiceImpl(CategoryRepo categoryRepo, AdvancedStructuredLogger advancedStructuredLogger) {
        this.categoryRepo = categoryRepo;
        this.advancedStructuredLogger = advancedStructuredLogger;
    }

    @Override
    public void createCategory(String name, String mainSection) {
        advancedStructuredLogger.logBuilder()
                .withEventType("CATEGORY_CREATE_START")
                .withMessage("Attempting to create a new category")
                .withField("categoryName", name)
                .withField("mainSection", mainSection)
                .withLevel("INFO")
                .log();

        Optional<Category> category = categoryRepo.findCategoryByName(name);
        if (category.isPresent()) {
            advancedStructuredLogger.logBuilder()
                    .withEventType("CATEGORY_CREATE_EXISTS")
                    .withMessage("Category already exists")
                    .withField("categoryName", name)
                    .withLevel("WARN")
                    .log();
            throw new AlreadyExistsException("Category with name " + name + " already exists");
        }

        try {
            Category newCategory = new Category();
            newCategory.setName(name);
            newCategory.setMainSection(MainSection.valueOf(mainSection));
            categoryRepo.saveAndFlush(newCategory);

            advancedStructuredLogger.logBuilder()
                    .withEventType("CATEGORY_CREATE_SUCCESS")
                    .withMessage("Category created successfully")
                    .withField("categoryName", name)
                    .withField("mainSection", mainSection)
                    .withLevel("INFO")
                    .log();
        } catch (Exception e) {
            advancedStructuredLogger.logBuilder()
                    .withEventType("CATEGORY_CREATE_ERROR")
                    .withMessage("Failed to create category")
                    .withField("categoryName", name)
                    .withField("mainSection", mainSection)
                    .withField("error", e.getMessage())
                    .withLevel("ERROR")
                    .log();
            throw e;
        }
    }

    @Override
    public void updateCategory(String name, String updatedName, String updatedMainSection) {
        advancedStructuredLogger.logBuilder()
                .withEventType("CATEGORY_UPDATE_START")
                .withMessage("Attempting to update category")
                .withField("existingName", name)
                .withField("updatedName", updatedName)
                .withField("updatedMainSection", updatedMainSection)
                .withLevel("INFO")
                .log();

        try {
            Category categoryToUpdate = categoryRepo.findCategoryByName(name)
                    .orElseThrow(() -> {
                        advancedStructuredLogger.logBuilder()
                                .withEventType("CATEGORY_UPDATE_NOT_FOUND")
                                .withMessage("Category not found for update")
                                .withField("existingName", name)
                                .withLevel("ERROR")
                                .log();
                        return new NotFoundException("Category with name '" + name + "' not found");
                    });

            categoryRepo.findCategoryByName(updatedName).ifPresent(existingCategory -> {
                if (!existingCategory.getId().equals(categoryToUpdate.getId())) {
                    advancedStructuredLogger.logBuilder()
                            .withEventType("CATEGORY_UPDATE_DUPLICATE")
                            .withMessage("Category name conflict during update")
                            .withField("updatedName", updatedName)
                            .withLevel("WARN")
                            .log();
                    throw new AlreadyExistsException("Category with name '" + updatedName + "' already exists");
                }
            });

            categoryToUpdate.setName(updatedName);
            categoryToUpdate.setMainSection(MainSection.valueOf(updatedMainSection));
            categoryRepo.saveAndFlush(categoryToUpdate);

            advancedStructuredLogger.logBuilder()
                    .withEventType("CATEGORY_UPDATE_SUCCESS")
                    .withMessage("Category updated successfully")
                    .withField("existingName", name)
                    .withField("updatedName", updatedName)
                    .withField("updatedMainSection", updatedMainSection)
                    .withLevel("INFO")
                    .log();
        } catch (Exception e) {
            advancedStructuredLogger.logBuilder()
                    .withEventType("CATEGORY_UPDATE_ERROR")
                    .withMessage("Failed to update category")
                    .withField("existingName", name)
                    .withField("updatedName", updatedName)
                    .withField("error", e.getMessage())
                    .withLevel("ERROR")
                    .log();
            throw e;
        }
    }

    @Override
    public void deleteCategory(String name) {
        advancedStructuredLogger.logBuilder()
                .withEventType("CATEGORY_DELETE_START")
                .withMessage("Attempting to delete category")
                .withField("categoryName", name)
                .withLevel("INFO")
                .log();

        try {
            Category categoryToDelete = categoryRepo.findCategoryByName(name)
                    .orElseThrow(() -> {
                        advancedStructuredLogger.logBuilder()
                                .withEventType("CATEGORY_DELETE_NOT_FOUND")
                                .withMessage("Category not found for deletion")
                                .withField("categoryName", name)
                                .withLevel("ERROR")
                                .log();
                        return new NotFoundException("Category with name '" + name + "' not found");
                    });

            categoryRepo.delete(categoryToDelete);

            advancedStructuredLogger.logBuilder()
                    .withEventType("CATEGORY_DELETE_SUCCESS")
                    .withMessage("Category deleted successfully")
                    .withField("categoryName", name)
                    .withLevel("INFO")
                    .log();
        } catch (Exception e) {
            advancedStructuredLogger.logBuilder()
                    .withEventType("CATEGORY_DELETE_ERROR")
                    .withMessage("Failed to delete category")
                    .withField("categoryName", name)
                    .withField("error", e.getMessage())
                    .withLevel("ERROR")
                    .log();
            throw e;
        }
    }
}
