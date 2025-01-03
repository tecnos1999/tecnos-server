package com.example.tecnosserver.itemcategory.web;

import com.example.tecnosserver.itemcategory.dto.ItemCategoryDTO;
import com.example.tecnosserver.itemcategory.model.ItemCategory;
import com.example.tecnosserver.itemcategory.service.ItemCategoryCommandServiceImpl;
import com.example.tecnosserver.itemcategory.service.ItemCategoryQueryServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin
@RequestMapping("/server/api/v1/itemcategory/")
@AllArgsConstructor
public class ItemCategoryControllerApi {

    private final ItemCategoryCommandServiceImpl itemCategoryCommandService;
    private final ItemCategoryQueryServiceImpl itemCategoryQueryService;

    @PostMapping("/create")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<String> createItemCategory(
            @RequestParam String name,
            @RequestParam String subCategory,
            @RequestParam String categoryName) {
        itemCategoryCommandService.createItemCategory(name, subCategory, categoryName);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body("ItemCategory '" + name + "' created successfully.");
    }

    @PutMapping("/update")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<String> updateItemCategory(
            @RequestParam String name,
            @RequestParam String updatedName,
            @RequestParam String subCategoryName,
            @RequestParam String categoryName,
            @RequestParam String updatedSubCategoryName,
            @RequestParam String updatedCategoryName) {

        itemCategoryCommandService.updateItemCategory(
                name,
                updatedName,
                subCategoryName,
                categoryName,
                updatedSubCategoryName,
                updatedCategoryName);
        return ResponseEntity.ok("ItemCategory updated from '" + name + "' to '" + updatedName + "'.");
    }

    @DeleteMapping("/delete")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<String> deleteItemCategory(
            @RequestParam String name,
            @RequestParam String subCategoryName,
            @RequestParam String categoryName) {
        itemCategoryCommandService.deleteItemCategory(name, subCategoryName, categoryName);
        return ResponseEntity.ok("ItemCategory '" + name + "' deleted successfully.");
    }


    @GetMapping("/all")
    public ResponseEntity<List<ItemCategoryDTO>> findAllItemCategories(
            @RequestParam(required = false) String subCategoryName,
            @RequestParam(required = false) String categoryName) {
        Optional<List<ItemCategoryDTO>> itemCategories = itemCategoryQueryService.findAllItemCategories(subCategoryName, categoryName);
        return itemCategories.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body(null));
    }
}
