package com.example.tecnosserver.category.web;

import com.example.tecnosserver.category.model.Category;
import com.example.tecnosserver.category.service.CategoryCommandServiceImpl;
import com.example.tecnosserver.category.service.CategoryQueryServiceImpl;
import com.example.tecnosserver.utils.AdvancedStructuredLogger;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin
@RequestMapping("/server/api/v1/category/")
@AllArgsConstructor
public class CategoryControllerApi {

    private final CategoryCommandServiceImpl categoryCommandService;
    private final CategoryQueryServiceImpl categoryQueryService;
    private final AdvancedStructuredLogger advancedStructuredLogger;

    @PostMapping("/create")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<String> createCategory(@RequestParam String name, @RequestParam String mainSection) {
        advancedStructuredLogger.logBuilder()
                .withEventType("CATEGORY_CREATE_REQUEST")
                .withMessage("Received request to create category")
                .withField("categoryName", name)
                .withField("mainSection", mainSection)
                .withLevel("INFO")
                .log();

        try {
            categoryCommandService.createCategory(name, mainSection);
            advancedStructuredLogger.logBuilder()
                    .withEventType("CATEGORY_CREATE_SUCCESS")
                    .withMessage("Category created successfully")
                    .withField("categoryName", name)
                    .withField("mainSection", mainSection)
                    .withLevel("INFO")
                    .log();
            return ResponseEntity.status(HttpStatus.CREATED).body("Category '" + name + "' created successfully.");
        } catch (Exception e) {
            advancedStructuredLogger.logBuilder()
                    .withEventType("CATEGORY_CREATE_ERROR")
                    .withMessage("Failed to create category")
                    .withField("categoryName", name)
                    .withField("mainSection", mainSection)
                    .withField("error", e.getMessage())
                    .withLevel("ERROR")
                    .log();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to create category: " + e.getMessage());
        }
    }

    @PutMapping("/update")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<String> updateCategory(@RequestParam String name, @RequestParam String updatedName, @RequestParam String updatedMainSection) {
        advancedStructuredLogger.logBuilder()
                .withEventType("CATEGORY_UPDATE_REQUEST")
                .withMessage("Received request to update category")
                .withField("existingName", name)
                .withField("updatedName", updatedName)
                .withField("updatedMainSection", updatedMainSection)
                .withLevel("INFO")
                .log();

        try {
            categoryCommandService.updateCategory(name, updatedName, updatedMainSection);
            advancedStructuredLogger.logBuilder()
                    .withEventType("CATEGORY_UPDATE_SUCCESS")
                    .withMessage("Category updated successfully")
                    .withField("existingName", name)
                    .withField("updatedName", updatedName)
                    .withField("updatedMainSection", updatedMainSection)
                    .withLevel("INFO")
                    .log();
            return ResponseEntity.ok("Category updated from '" + name + "' to '" + updatedName + "'.");
        } catch (Exception e) {
            advancedStructuredLogger.logBuilder()
                    .withEventType("CATEGORY_UPDATE_ERROR")
                    .withMessage("Failed to update category")
                    .withField("existingName", name)
                    .withField("updatedName", updatedName)
                    .withField("error", e.getMessage())
                    .withLevel("ERROR")
                    .log();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to update category: " + e.getMessage());
        }
    }

    @DeleteMapping("/delete")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<String> deleteCategory(@RequestParam String name) {
        advancedStructuredLogger.logBuilder()
                .withEventType("CATEGORY_DELETE_REQUEST")
                .withMessage("Received request to delete category")
                .withField("categoryName", name)
                .withLevel("INFO")
                .log();

        try {
            categoryCommandService.deleteCategory(name);
            advancedStructuredLogger.logBuilder()
                    .withEventType("CATEGORY_DELETE_SUCCESS")
                    .withMessage("Category deleted successfully")
                    .withField("categoryName", name)
                    .withLevel("INFO")
                    .log();
            return ResponseEntity.ok("Category '" + name + "' deleted successfully.");
        } catch (Exception e) {
            advancedStructuredLogger.logBuilder()
                    .withEventType("CATEGORY_DELETE_ERROR")
                    .withMessage("Failed to delete category")
                    .withField("categoryName", name)
                    .withField("error", e.getMessage())
                    .withLevel("ERROR")
                    .log();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to delete category: " + e.getMessage());
        }
    }

    @GetMapping("/find")
    public ResponseEntity<Category> findCategoryByName(@RequestParam String name) {
        advancedStructuredLogger.logBuilder()
                .withEventType("CATEGORY_FIND_REQUEST")
                .withMessage("Received request to find category by name")
                .withField("categoryName", name)
                .withLevel("INFO")
                .log();

        try {
            Optional<Category> category = categoryQueryService.findCategoryByName(name);
            if (category.isPresent()) {
                advancedStructuredLogger.logBuilder()
                        .withEventType("CATEGORY_FIND_SUCCESS")
                        .withMessage("Category found successfully")
                        .withField("categoryName", name)
                        .withField("categoryId", category.get().getId())
                        .withLevel("INFO")
                        .log();
                return ResponseEntity.ok(category.get());
            } else {
                advancedStructuredLogger.logBuilder()
                        .withEventType("CATEGORY_FIND_NOT_FOUND")
                        .withMessage("Category not found")
                        .withField("categoryName", name)
                        .withLevel("WARN")
                        .log();
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }
        } catch (Exception e) {
            advancedStructuredLogger.logBuilder()
                    .withEventType("CATEGORY_FIND_ERROR")
                    .withMessage("Failed to find category")
                    .withField("categoryName", name)
                    .withField("error", e.getMessage())
                    .withLevel("ERROR")
                    .log();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/findAll")
    public ResponseEntity<List<Category>> findAllCategories() {
        advancedStructuredLogger.logBuilder()
                .withEventType("CATEGORY_FIND_ALL_REQUEST")
                .withMessage("Received request to find all categories")
                .withLevel("INFO")
                .log();

        try {
            Optional<List<Category>> categories = categoryQueryService.findAllCategories();
            if (categories.isPresent() && !categories.get().isEmpty()) {
                advancedStructuredLogger.logBuilder()
                        .withEventType("CATEGORY_FIND_ALL_SUCCESS")
                        .withMessage("Categories found successfully")
                        .withField("categoryCount", categories.get().size())
                        .withLevel("INFO")
                        .log();
                return ResponseEntity.ok(categories.get());
            } else {
                advancedStructuredLogger.logBuilder()
                        .withEventType("CATEGORY_FIND_ALL_EMPTY")
                        .withMessage("No categories found")
                        .withLevel("WARN")
                        .log();
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }
        } catch (Exception e) {
            advancedStructuredLogger.logBuilder()
                    .withEventType("CATEGORY_FIND_ALL_ERROR")
                    .withMessage("Failed to find categories")
                    .withField("error", e.getMessage())
                    .withLevel("ERROR")
                    .log();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}
