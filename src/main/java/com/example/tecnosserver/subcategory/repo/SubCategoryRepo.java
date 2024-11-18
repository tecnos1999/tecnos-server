package com.example.tecnosserver.subcategory.repo;

import com.example.tecnosserver.category.model.Category;
import com.example.tecnosserver.subcategory.model.SubCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SubCategoryRepo extends JpaRepository<SubCategory,Long> {

    Optional<SubCategory> findSubCategoryByName(String name);

    boolean existsByNameAndCategory(String name, Category category);

    Optional<SubCategory> findByNameAndCategory(String name, Category category);
}
