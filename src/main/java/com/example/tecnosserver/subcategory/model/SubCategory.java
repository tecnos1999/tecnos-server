package com.example.tecnosserver.subcategory.model;
import com.example.tecnosserver.products.model.Product;
import com.example.tecnosserver.category.model.Category;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;


import java.time.LocalDateTime;

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
    private Long id;

    @Column(name = "name", nullable = false,unique = true)
    private String name;

    @ManyToOne
    @JoinColumn(name = "category_id",nullable = false)
    private Category category;

    @OneToOne(mappedBy = "subCategory", cascade = CascadeType.ALL)
    private Product product;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}

