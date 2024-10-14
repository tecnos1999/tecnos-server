package com.example.tecnosserver.products.model;
import com.example.tecnosserver.subcategory.model.SubCategory;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity(name = "Product")
@Table(name = "products")
@AllArgsConstructor
@SuperBuilder
@NoArgsConstructor
@Data
public class Product {

    @Id
    @SequenceGenerator(name = "product_sequence", sequenceName = "product_sequence", allocationSize = 1)
    @GeneratedValue(generator = "product_sequence", strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(name="name", nullable = false)
    private String name;

    @Column(name="sku", nullable = false, unique = true)
    private String sku;

    @Column(name="description", nullable = false)
    private String description;

    @Column(name="price", nullable = false)
    private double price;

    @Column(name="stock", nullable = false)
    private int stock;

    @Column(name="image", nullable = false)
    private String image;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @OneToOne
    @JoinColumn(name = "subcategory_id", nullable = false)
    private SubCategory subCategory;
}
