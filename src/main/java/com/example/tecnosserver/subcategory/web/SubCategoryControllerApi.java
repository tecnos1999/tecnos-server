package com.example.tecnosserver.subcategory.web;

import com.example.tecnosserver.subcategory.model.SubCategory;
import com.example.tecnosserver.subcategory.service.SubCategoryCommandServiceImpl;
import com.example.tecnosserver.subcategory.service.SubCategoryQueryServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin
@RequestMapping("/server/api/v1/subcategory/")
@AllArgsConstructor
public class SubCategoryControllerApi {

    private final SubCategoryCommandServiceImpl subCategoryCommandService;
    private final SubCategoryQueryServiceImpl subCategoryQueryService;

    @PostMapping("/create")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<String> createSubCategory(@RequestParam String name, @RequestParam String category) {
        subCategoryCommandService.createSubCategory(name, category);
        return ResponseEntity.status(HttpStatus.CREATED).body("Subcategory '" + name + "' created successfully.");
    }

    @PutMapping("/update")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<String> updateSubCategory(@RequestParam String name, @RequestParam String updatedName) {
        subCategoryCommandService.updateSubCategory(name, updatedName);
        return ResponseEntity.ok("Subcategory updated from '" + name + "' to '" + updatedName + "'.");
    }

    @DeleteMapping("/delete")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<String> deleteSubCategory(@RequestParam String name) {
        subCategoryCommandService.deleteSubCategory(name);
        return ResponseEntity.ok("Subcategory '" + name + "' deleted successfully.");
    }

    @GetMapping("/find")
    public ResponseEntity<SubCategory> findSubCategoryByName(@RequestParam String name) {
        Optional<SubCategory> subCategory = subCategoryQueryService.findSubCategoryByName(name);
        return subCategory.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body(null));
    }

    @GetMapping("/all")
    public ResponseEntity<List<SubCategory>> findAllSubCategories() {
        Optional<List<SubCategory>> subCategories = subCategoryQueryService.findAllSubCategories();
        return subCategories.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body(null));
    }
}
