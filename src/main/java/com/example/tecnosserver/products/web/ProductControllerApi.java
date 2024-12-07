package com.example.tecnosserver.products.web;

import com.example.tecnosserver.products.dto.ProductDTO;
import com.example.tecnosserver.products.model.Product;
import com.example.tecnosserver.products.service.ProductCommandService;
import com.example.tecnosserver.products.service.ProductQueryService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
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
    public ResponseEntity<ProductDTO> findProductBySku(@PathVariable String sku) {
        Optional<ProductDTO> productOpt = productQueryService.findProductBySku(sku);
        return ResponseEntity.ok(productOpt.get());
    }

    @GetMapping("/all")
    public ResponseEntity<List<ProductDTO>> findAllProducts() {
        Optional<List<ProductDTO>> productsOpt = productQueryService.findAllProducts();
        return ResponseEntity.ok(productsOpt.get());
    }

    @GetMapping("/category/{category}/subcategory/{subCategory}")
    public ResponseEntity<List<ProductDTO>> findAllByCategoryAndSubCategory(
            @PathVariable String category,
            @PathVariable String subCategory) {
        Optional<List<ProductDTO>> productsOpt = productQueryService.findAllByCategoryAndSubCategory(category, subCategory);
        return ResponseEntity.ok(productsOpt.get());
    }

    @GetMapping("/category/{category}/subcategory/{subCategory}/item-category/{itemCategory}")
    public ResponseEntity<List<ProductDTO>> findAllByCategoryAndSubCategoryAndItemCategory(
            @PathVariable String category,
            @PathVariable String subCategory,
            @PathVariable String itemCategory) {
        Optional<List<ProductDTO>> productsOpt = productQueryService.findAllByCategoryAndSubCategoryAndItemCategory(category, subCategory, itemCategory);
        return ResponseEntity.ok(productsOpt.get());
    }

    @GetMapping("/partner/{partnerName}")
    public ResponseEntity<List<ProductDTO>> findAllByPartnerName(@PathVariable String partnerName) {
        Optional<List<ProductDTO>> productsOpt = productQueryService.findAllByPartnerName(partnerName);
        return ResponseEntity.ok(productsOpt.get());
    }
}
