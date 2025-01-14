package com.example.tecnosserver.products.web;

import com.example.tecnosserver.products.dto.ProductDTO;
import com.example.tecnosserver.products.service.ProductCommandService;
import com.example.tecnosserver.products.service.ProductQueryService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/server/api/v1/product/")
@AllArgsConstructor
@Slf4j
public class ProductControllerApi {

    private final ProductCommandService productCommandService;
    private final ProductQueryService productQueryService;

    @PostMapping(consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
    public ResponseEntity<String> createProduct(
            @RequestPart("productDTO") ProductDTO productDTO,
            @RequestPart(value = "images", required = false) List<MultipartFile> imageFiles,
            @RequestPart(value = "broschure", required = false) MultipartFile broschureFile,
            @RequestPart(value = "tehnic", required = false) MultipartFile tehnicFile) {
        productCommandService.createProduct(productDTO, imageFiles, broschureFile, tehnicFile);
        return ResponseEntity.ok("Product created successfully");
    }

    @PutMapping(value = "/{sku}", consumes = {"multipart/form-data"})
    public ResponseEntity<String> updateProduct(
            @PathVariable String sku,
            @RequestPart("productDTO") String productDTOJson,
            @RequestPart(value = "images", required = false) List<MultipartFile> imageFiles,
            @RequestPart(value = "broschure", required = false) MultipartFile broschureFile,
            @RequestPart(value = "tehnic", required = false) MultipartFile tehnicFile) {
        log.info("Received ProductDTO JSON: {}", productDTOJson);

        ObjectMapper objectMapper = new ObjectMapper();
        ProductDTO updatedProductDTO;
        try {
            updatedProductDTO = objectMapper.readValue(productDTOJson, ProductDTO.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Invalid ProductDTO JSON", e);
        }

        log.info("Converted ProductDTO: {}", updatedProductDTO);
        productCommandService.updateProduct(sku, updatedProductDTO, imageFiles, broschureFile, tehnicFile);
        return ResponseEntity.ok("Product updated successfully");
    }



    @DeleteMapping("/{sku}")
    public ResponseEntity<String> deleteProduct(@PathVariable String sku) {
        productCommandService.deleteProduct(sku);
        return ResponseEntity.ok("Product deleted successfully");
    }

    @GetMapping("/{sku}")
    public ResponseEntity<ProductDTO> findProductBySku(@PathVariable String sku) {
        return productQueryService.findProductBySku(sku)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new RuntimeException("Product not found"));
    }

    @GetMapping("/all")
    public ResponseEntity<List<ProductDTO>> findAllProducts() {
        return productQueryService.findAllProducts()
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new RuntimeException("No products found"));
    }

    @GetMapping("/category/{category}/subcategory/{subCategory}")
    public ResponseEntity<List<ProductDTO>> findAllByCategoryAndSubCategory(
            @PathVariable String category,
            @PathVariable String subCategory) {
        return productQueryService.findAllByCategoryAndSubCategory(category, subCategory)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new RuntimeException("No products found for given category and subcategory"));
    }

    @GetMapping("/category/{category}/subcategory/{subCategory}/item-category/{itemCategory}")
    public ResponseEntity<List<ProductDTO>> findAllByCategoryAndSubCategoryAndItemCategory(
            @PathVariable String category,
            @PathVariable String subCategory,
            @PathVariable String itemCategory) {
        return productQueryService.findAllByCategoryAndSubCategoryAndItemCategory(category, subCategory, itemCategory)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new RuntimeException("No products found for given category, subcategory, and item category"));
    }

    @GetMapping("/partner/{partnerName}")
    public ResponseEntity<List<ProductDTO>> findAllByPartnerName(@PathVariable String partnerName) {
        return productQueryService.findAllByPartnerName(partnerName)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new RuntimeException("No products found for the given partner name"));
    }


    @GetMapping("/tag/{tagName}")
    public ResponseEntity<List<ProductDTO>> findAllByTagName(@PathVariable String tagName) {
        return productQueryService.findAllByTagName(tagName)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new RuntimeException("No products found for the given tag name"));
    }

    @GetMapping("/tags")
    public ResponseEntity<List<ProductDTO>> findAllByTagsName(@RequestParam List<String> tagNames) {
        return productQueryService.findAllByTagsName(tagNames)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new RuntimeException("No products found for the given tags"));
    }

    @GetMapping("/sku")
    public ResponseEntity<List<ProductDTO>> findBySkuIn(@RequestParam List<String> skus) {
        log.info("Fetching products for SKUs: {}", String.join(", ", skus));
        List<ProductDTO> products = productQueryService.findBySkuIn(skus);
        return ResponseEntity.ok(products);
    }

}
