package com.example.tecnosserver.products.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductDTO {
    private String sku;
    private String name;
    private String description;
    private String subCategory;
    private int stock;
    private String image;
}

