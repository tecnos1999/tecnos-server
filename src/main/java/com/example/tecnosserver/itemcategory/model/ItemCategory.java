package com.example.tecnosserver.itemcategory.model;

import com.example.tecnosserver.category.model.Category;
import com.example.tecnosserver.products.model.Product;
import com.example.tecnosserver.subcategory.model.SubCategory;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@Entity(name = "ItemCategory")
@Table(name = "item_categories")
@AllArgsConstructor
@SuperBuilder
@NoArgsConstructor
@Data
public class ItemCategory {

    @Id
    @SequenceGenerator(name = "itemcategory_sequence", sequenceName = "itemcategory_sequence", allocationSize = 1)
    @GeneratedValue(generator = "itemcategory_sequence", strategy = GenerationType.SEQUENCE)
    @JsonIgnore
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @ManyToOne
    @JoinColumn(name = "subcategory_id", nullable = false)
    @JsonBackReference
    private SubCategory subCategory;

    @JsonProperty("subcategoryName")
    public String getSubCategoryName() {
        return subCategory != null ? subCategory.getName() : null;
    }

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    @JsonBackReference(value = "category-itemcategory")
    private Category category;

    @JsonProperty("categoryName")
    public String getCategoryName() {
        return category != null ? category.getName() : null;
    }

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;


    @OneToMany(mappedBy = "itemCategory", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonBackReference
    private List<Product> products;
}

