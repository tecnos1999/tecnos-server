package com.example.tecnosserver.products.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

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

    @Column(name="sku", nullable = false)
    private String sku;

    @Column(name="description", nullable = false)
    private String description;

    @Column(name="price", nullable = false)

    private double price;

    @Column(name="stock", nullable = false)
    private int stock;

    @Column(name="image", nullable = false)
    private String image;




}
