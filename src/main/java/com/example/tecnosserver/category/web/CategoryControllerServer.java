package com.example.tecnosserver.category.web;

import com.example.tecnosserver.category.model.Category;
import com.example.tecnosserver.category.service.CategoryCommandServiceImpl;
import com.example.tecnosserver.category.service.CategoryQueryServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@CrossOrigin
@RequestMapping("/server/api/v1/category/")
@AllArgsConstructor
public class CategoryControllerServer {

    private final CategoryCommandServiceImpl categoryCommandService;
    private final CategoryQueryServiceImpl categoryQueryService;

    @PostMapping("/create")
    public ResponseEntity<String> createCategory(@RequestParam String name) {
        categoryCommandService.createCategory(name);
        return ResponseEntity.status(HttpStatus.CREATED).body("Category '" + name + "' created successfully.");
    }

    @PutMapping("/update")
    public ResponseEntity<String> updateCategory(@RequestParam String name, @RequestParam String updatedName) {
        categoryCommandService.updateCategory(name, updatedName);
        return ResponseEntity.ok("Category updated from '" + name + "' to '" + updatedName + "'.");
    }

    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteCategory(@RequestParam String name) {
        categoryCommandService.deleteCategory(name);
        return ResponseEntity.ok("Category '" + name + "' deleted successfully.");
    }

    @GetMapping("/find")
    public ResponseEntity<Category> findCategoryByName(@RequestParam String name) {
        Optional<Category> category = categoryQueryService.findCategoryByName(name);
        return category.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body(null));
    }
}

