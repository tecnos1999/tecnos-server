package com.example.tecnosserver.products.repo;

import com.example.tecnosserver.products.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepo extends JpaRepository<Product, Long>{
    Optional<Product> findProductBySku(String sku);

    @Query("SELECT p FROM Product p WHERE p.category.name = ?1 AND p.subCategory.name = ?2")
    Optional<List<Product>> findAllByCategoryAndSubCategory(String category, String subCategory);

    @Query("SELECT p FROM Product p WHERE p.category.name = ?1 AND p.subCategory.name = ?2 AND p.itemCategory.name = ?3")
    Optional<List<Product>> findAllByCategoryAndSubCategoryAndItemCategory(String category, String subCategory, String itemCategory);

    @Query("SELECT p FROM Product p WHERE p.partner.name = :partnerName")
    Optional<List<Product>> findAllByPartnerName(String partnerName);

    @Query("SELECT p FROM Product p JOIN p.tags t WHERE t.name = :tagName")
    Optional<List<Product>> findAllByTagName(String tagName);

    @Query("SELECT DISTINCT p FROM Product p JOIN p.tags t WHERE t.name IN :tagNames")
    Optional<List<Product>> findAllByTagsName(List<String> tagNames);
}
