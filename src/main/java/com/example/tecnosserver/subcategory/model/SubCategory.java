package com.example.tecnosserver.subcategory.model;
import com.example.tecnosserver.itemcategory.model.ItemCategory;
import com.example.tecnosserver.products.model.Product;
import com.example.tecnosserver.category.model.Category;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;


import java.time.LocalDateTime;
import java.util.List;

@Entity(name = "SubCategory")
@Table(name = "subcategories")
@AllArgsConstructor
@SuperBuilder
@NoArgsConstructor
@Data
public class SubCategory {

    @Id
    @SequenceGenerator(name = "subcategory_sequence", sequenceName = "subcategory_sequence", allocationSize = 1)
    @GeneratedValue(generator = "subcategory_sequence", strategy = GenerationType.SEQUENCE)
    @JsonIgnore
    private Long id;

    @Column(name = "name", nullable = false,unique = true)
    private String name;

    @ManyToOne
    @JoinColumn(name = "category_id",nullable = false)
    @JsonIgnore
    @JsonBackReference
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


    @OneToMany(mappedBy = "subCategory", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<ItemCategory> itemCategories;

    @OneToMany(mappedBy = "subCategory", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference(value = "subcategory-product")
    private List<Product> products;

}

