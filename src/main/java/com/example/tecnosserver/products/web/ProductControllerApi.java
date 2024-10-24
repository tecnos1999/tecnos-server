package com.example.tecnosserver.products.web;

import com.example.tecnosserver.products.dto.ProductDTO;
import com.example.tecnosserver.products.model.Product;
import com.example.tecnosserver.products.service.ProductCommandService;
import com.example.tecnosserver.products.service.ProductQueryService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@CrossOrigin
@RequestMapping("/server/api/v1/product/")
@AllArgsConstructor
public class ProductControllerApi {

    private final ProductCommandService productCommandService;
    private final ProductQueryService productQueryService;

    @PostMapping
    public ResponseEntity<String> createProduct(@RequestBody ProductDTO productDTO) {
        productCommandService.createProduct(productDTO);
        return ResponseEntity.ok("Product created successfully");
    }

    @PutMapping("/{sku}")
    public ResponseEntity<String> updateProduct(@PathVariable String sku, @RequestBody ProductDTO updatedProductDTO) {
        productCommandService.updateProduct(sku, updatedProductDTO);
        return ResponseEntity.ok("Product updated successfully");
    }

    @DeleteMapping("/{sku}")
    public ResponseEntity<String> deleteProduct(@PathVariable String sku) {
        productCommandService.deleteProduct(sku);
        return ResponseEntity.ok("Product deleted successfully");
    }

    @GetMapping("/{sku}")
    public ResponseEntity<Product> findProductBySku(@PathVariable String sku) {
        Optional<Product> productOpt = productQueryService.findProductBySku(sku);
        return ResponseEntity.ok(productOpt.get());
    }
}
