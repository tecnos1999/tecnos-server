package com.example.tecnosserver.itemcategory.web;

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
    public ResponseEntity<String> createItemCategory(@RequestParam String name, @RequestParam String subCategory) {
        itemCategoryCommandService.createItemCategory(name, subCategory);
        return ResponseEntity.status(HttpStatus.CREATED).body("ItemCategory '" + name + "' created successfully.");
    }

    @PutMapping("/update")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<String> updateItemCategory(@RequestParam String name, @RequestParam String updatedName) {
        itemCategoryCommandService.updateItemCategory(name, updatedName);
        return ResponseEntity.ok("ItemCategory updated from '" + name + "' to '" + updatedName + "'.");
    }

    @DeleteMapping("/delete")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<String> deleteItemCategory(@RequestParam String name) {
        itemCategoryCommandService.deleteItemCategory(name);
        return ResponseEntity.ok("ItemCategory '" + name + "' deleted successfully.");
    }

    @GetMapping("/find")
    public ResponseEntity<ItemCategory> findItemCategoryByName(@RequestParam String name) {
        Optional<ItemCategory> itemCategory = itemCategoryQueryService.findItemCategoryByName(name);
        return itemCategory.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body(null));
    }

    @GetMapping("/all")
    public ResponseEntity<List<ItemCategory>> findAllItemCategories() {
        Optional<List<ItemCategory>> itemCategories = itemCategoryQueryService.findAllItemCategories();
        return itemCategories.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body(null));
    }
}
