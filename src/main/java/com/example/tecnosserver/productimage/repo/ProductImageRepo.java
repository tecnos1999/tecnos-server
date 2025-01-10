package com.example.tecnosserver.productimage.repo;

import com.example.tecnosserver.productimage.model.ProductImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductImageRepo extends JpaRepository<ProductImage, Long> {
}
