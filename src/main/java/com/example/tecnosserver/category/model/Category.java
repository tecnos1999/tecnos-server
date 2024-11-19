package com.example.tecnosserver.category.model;

import com.example.tecnosserver.itemcategory.model.ItemCategory;
import com.example.tecnosserver.products.model.Product;
import com.example.tecnosserver.subcategory.model.SubCategory;
import com.example.tecnosserver.utils.MainSection;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@Entity(name = "Category")
@Table(name = "categories")
@AllArgsConstructor
@SuperBuilder
@NoArgsConstructor
@Data
public class Category {

    @Id
    @SequenceGenerator(name = "category_sequence", sequenceName = "category_sequence", allocationSize = 1)
    @GeneratedValue(generator = "category_sequence", strategy = GenerationType.SEQUENCE)
    @JsonIgnore
    private Long id;

    @Column(name = "name", nullable = false,unique = true)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "main_section", nullable = false)
    private MainSection mainSection;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;


    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<SubCategory> subCategories;

    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference(value = "category-itemcategory")
    private List<ItemCategory> itemCategories;


    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference(value = "category-product")
    private List<Product> products;
}
