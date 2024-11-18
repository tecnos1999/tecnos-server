package com.example.tecnosserver.itemcategory.repo;

import com.example.tecnosserver.category.model.Category;
import com.example.tecnosserver.itemcategory.model.ItemCategory;
import com.example.tecnosserver.subcategory.model.SubCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ItemCategoryRepo extends JpaRepository<ItemCategory, Long> {
    Optional<ItemCategory> findByName(String name);
    boolean existsByNameAndSubCategory(String name, SubCategory subCategory);
    boolean existsByNameAndSubCategoryAndCategory(String name, SubCategory subCategory, Category category);
    Optional<ItemCategory> findByNameAndSubCategoryAndCategory(String name, SubCategory subCategory, Category category);
}
