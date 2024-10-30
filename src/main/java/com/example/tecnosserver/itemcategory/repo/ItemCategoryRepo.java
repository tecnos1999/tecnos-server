package com.example.tecnosserver.itemcategory.repo;

import com.example.tecnosserver.itemcategory.model.ItemCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ItemCategoryRepo extends JpaRepository<ItemCategory, Long> {
    Optional<ItemCategory> findByName(String name);
}
