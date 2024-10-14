package com.example.tecnosserver.products.repo;

import com.example.tecnosserver.products.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductRepo extends JpaRepository<Product, Long>{

    Optional<Product> findProductBySku(String sku);



}
