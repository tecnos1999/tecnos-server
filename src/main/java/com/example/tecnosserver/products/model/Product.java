package com.example.tecnosserver.products.model;
import com.example.tecnosserver.category.model.Category;
import com.example.tecnosserver.itemcategory.model.ItemCategory;
import com.example.tecnosserver.subcategory.model.SubCategory;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import com.example.tecnosserver.image.model.Image;
import java.time.LocalDateTime;
import java.util.List;

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

    @Column(name="broschure")
    private String broschure;

    @Column(name="tehnic")
    private String tehnic;

    @Column(name="catalog")
    private String catalog;

    @Column(name="link_video")
    private String linkVideo;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @ManyToOne
    @JoinColumn(name = "item_category_id", referencedColumnName = "id")
    @JsonBackReference(value = "item-category-product")
    private ItemCategory itemCategory;

    @ManyToOne
    @JoinColumn(name = "category_id")
    @JsonBackReference(value = "category-product")
    private Category category;

    @ManyToOne
    @JoinColumn(name = "subcategory_id")
    @JsonBackReference(value = "subcategory-product")
    private SubCategory subCategory;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "product_id")
    private List<Image> images;
}
