package com.example.tecnosserver.itemcategory.repo;

import com.example.tecnosserver.category.model.Category;
import com.example.tecnosserver.itemcategory.model.ItemCategory;
import com.example.tecnosserver.subcategory.model.SubCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ItemCategoryRepo extends JpaRepository<ItemCategory, Long> {
    Optional<ItemCategory> findByNameAndSubCategoryAndCategory(String name, SubCategory subCategory, Category category);

    boolean existsByNameAndSubCategoryAndCategory(String name, SubCategory subCategory, Category category);

    List<ItemCategory> findAllBySubCategoryAndCategory(SubCategory subCategory, Category category);
}
