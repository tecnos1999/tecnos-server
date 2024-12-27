package com.example.tecnosserver.products.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProductDTO {
    private String sku;
    private String name;
    private String description;
    private String itemCategory;
    private String category;
    private String subCategory;
    private List<String> images;
    private List<String> imagesToKeep;
    private List<String> imagesToRemove;
    private String broschure;
    private String tehnic;
    private String linkVideo;
    private String partnerName;
    private List<String> tags;
}
