package com.example.tecnosserver.subcategory.repo;

import com.example.tecnosserver.subcategory.model.SubCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SubCategoryRepo extends JpaRepository<SubCategory,Long> {

    Optional<SubCategory> findSubCategoryByName(String name);
}
