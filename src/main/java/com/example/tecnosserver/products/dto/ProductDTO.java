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
    private String itemCategory;
    private String image;
    private String broschure;
    private String tehnic;
    private String catalog;
    private String linkVideo;
}

